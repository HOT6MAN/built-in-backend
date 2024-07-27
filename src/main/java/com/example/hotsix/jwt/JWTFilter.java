package com.example.hotsix.jwt;

import ch.qos.logback.core.spi.ErrorCodes;
import com.example.hotsix.dto.common.APIResponse;
import com.example.hotsix.dto.common.ErrorResponse;
import com.example.hotsix.dto.common.ProcessResponse;
import com.example.hotsix.enums.Process;
import com.example.hotsix.exception.BuiltInException;
import com.example.hotsix.oauth.dto.CustomOAuth2User;
import com.example.hotsix.oauth.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JWT 필터");
        log.info("request url: {}", request.getRequestURI());
        log.info("request cookies: {}", request.getCookies());


        String accessToken = null;
        String refreshToken = null;
        String authorization = null;
        //처음 로그인시 access토큰이 쿠키에 있어서 쿠키들을 불러온뒤 Authorizaiton key에 담긴 쿠키를 찾음
        Cookie[] cookies = request.getCookies();
        log.info("cookies: {}", cookies);
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("access")) {
                    log.info("access cookie: {}", cookie.getValue());
                    authorization = cookie.getValue();
                }else if(cookie.getName().equals("refresh")){
                    refreshToken = cookie.getValue();
                    log.info("refresh token: {}", refreshToken);
                    filterChain.doFilter(request, response);
                    return;

                }
            }

            accessToken = authorization;
        }

        if(accessToken == null && refreshToken == null) {
            filterChain.doFilter(request, response);
            return;
        }



        if (accessToken == null) {
            // Authorizaion헤더에 토큰이 없음
            if(request.getHeader("Authorization")==null){
                try{
                    log.info("Authorizaion헤더에 토큰이 없음");
                    //filterChain.doFilter(request, response);
                    throw new BuiltInException(Process.INVALID_USER);
                }catch (BuiltInException e){
                    jwtExceptionHandler(response, e);
                    return;
                }
            }
            // Authorizaion헤더에 토큰이 있다면 access토큰 얻어오기
            else{
                log.info("Authorizaion헤더에 토큰이 있다면 access토큰 얻어오기");
                authorization = request.getHeader("Authorization");
                accessToken = authorization;

                // accesstoken이 있지만 로그아웃해서 블랙처리된 경우
                if(authorization!=null && redisTemplate.hasKey(authorization)) {
                    log.info("로그아웃된 access token");
                    try{
                        //filterChain.doFilter(request, response);
                        throw new BuiltInException(Process.INVALID_USER);
                    }catch (BuiltInException e){
                        jwtExceptionHandler(response, e);
                        return;
                    }
                }


                //토큰 소멸 시간 검증
                try{
                    jwtUtil.isExpired(accessToken) ;
                }catch (ExpiredJwtException e){
                    //response body
                    log.info("Access 토큰 만료");
//            log.info(e.getMessage());
//            BuiltInException exception = new BuiltInException(Process.EXPIRED_TOKEN);
//            jwtExceptionHandler(response,exception);

                    PrintWriter writer = response.getWriter();
                    writer.println("access toekn is expired");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }catch( IllegalArgumentException e){
                    BuiltInException exception = new BuiltInException(Process.EXPIRED_TOKEN);
                    jwtExceptionHandler(response,exception);
                    return;
                }

                //토큰 카테고리가 acceess인지 확인
                String category = jwtUtil.getCategory(accessToken);

                if(!category.equals("access")){
                    log.info("카테고리가 access가 아니다");
                    try{
                        throw new BuiltInException(Process.INVALID_USER);
                    }catch (BuiltInException e){
                        jwtExceptionHandler(response, e);
                        return;
                    }
                }
            }
        }

        //토큰에서 id, username과 role 획득
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);
        Long id = jwtUtil.getId(accessToken);
        String name = jwtUtil.getName(accessToken);

        //userDTO를 생성하여 값 set
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setRole(role);
        userDTO.setId(id);
        userDTO.setName(name);

        //UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);
        log.info("customOAuth2User: {}", customOAuth2User);
        //스프링 시큐리티 인증 토큰 생성

        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        log.info("authToken: {}", authToken.getPrincipal().toString());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);



        filterChain.doFilter(request, response);





    }

    public void jwtExceptionHandler(HttpServletResponse response, BuiltInException error) {
        log.info("error: {}", error.getProcess());
        log.info("error: {}", error.getProcess().getMessage());
        log.info("error: {}", error.getProcess().getHttpStatus().value());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .process(error.getProcess())
                .build();
        log.info("errorResponse: {}", errorResponse.getProcess());
        APIResponse<Object> apiResponse = APIResponse.builder()
                .process(ProcessResponse.from(errorResponse.getProcess()))
                .build();


        response.setStatus(errorResponse.getProcess().getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(apiResponse);
            log.info("json: {}", json);
            response.getWriter().write(json);
            return;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
 
}
