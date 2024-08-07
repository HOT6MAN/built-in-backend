package com.example.hotsix.jwt;

import ch.qos.logback.core.spi.ErrorCodes;
import com.example.hotsix.dto.common.APIResponse;
import com.example.hotsix.dto.common.ErrorResponse;
import com.example.hotsix.dto.common.ProcessResponse;
import com.example.hotsix.enums.Process;
import com.example.hotsix.exception.BuiltInException;
import com.example.hotsix.oauth.dto.CustomOAuth2User;
import com.example.hotsix.oauth.dto.UserDTO;
import com.example.hotsix.service.auth.LoginService;
import com.example.hotsix.service.auth.LogoutService;
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
    //private final RedisTemplate<String, String> redisTemplate;
    private final LogoutService logoutService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JWT 필터");
        log.info("request url: {}", request.getRequestURI());
        log.info("request cookies: {}", (Object[]) request.getCookies());
        if(request.getRequestURI().startsWith("/ws/log")){
            filterChain.doFilter(request,response);
            return;
        }
        String requestURL = request.getRequestURI();


        if (requestURL.startsWith("/hot6man/swagger-ui.html") ||
                requestURL.startsWith("/hot6man/swagger-ui/") ||
                requestURL.startsWith("/hot6man/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = null;
        String refreshToken = null;
        String authorization = null;

        refreshToken = getRefreshToken(request);
        authorization = getAccessTokenFromHeader(request);
        log.info("refreshToken: {}", refreshToken);
        log.info("authorization: {}", authorization);
        // Authorizaion헤더에 토큰이 없음
//        if(authorization==null){
//            try{
//                log.info("Authorizaion헤더에 토큰이 없음");
//                //filterChain.doFilter(request, response);
//                throw new BuiltInException(Process.INVALID_USER);
//            }catch (BuiltInException e){
//                jwtExceptionHandler(response, e);
//                return;
//            }
//        }
        //일반 헤더 요청
        if(authorization!=null && refreshToken==null){
            log.info("일반 헤더 요청");
            accessToken = authorization;

            // accesstoken이 있지만 로그아웃해서 블랙처리된 경우
            if(accessToken!=null && logoutService.isTokenInRedis(accessToken)) {
                log.info("로그아웃된 access token");
                try{
                    throw new BuiltInException(Process.INVALID_TOKEN);
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

//                PrintWriter writer = response.getWriter();
//                writer.println("access toekn is expired");
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                BuiltInException exception = new BuiltInException(Process.EXPIRED_TOKEN);
                jwtExceptionHandler(response,exception);
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
                    throw new BuiltInException(Process.INVALID_TOKEN);
                }catch (BuiltInException e){
                    jwtExceptionHandler(response, e);
                    return;
                }
            }
            log.info("일반 헤더 요청 JWT 필터 끝");
            setSecurityContext(request, response, filterChain, accessToken);
            return;
        }
        // reissue요청
        else if(refreshToken!=null && authorization==null){
            log.info("리이슈 요청 JWT 필터 끝");
            filterChain.doFilter(request, response);
            return;
        }


        // 처음 로그인시 access쿠키 헤더 전환
        accessToken = getAccessToeknFromCookie(request);
        log.info("accessToken 쿠키: {}", accessToken);

        if(accessToken!=null){
            log.info("처음 로그인 JWT 필터 끝");
            setSecurityContext(request, response, filterChain, accessToken);
            return;
        }


        // 가입, 로그인 등 처음에 아무 토큰도 없을 떄
        if(accessToken==null && refreshToken==null){
            log.info("가입, 로그인 JWT 필터 끝");
            filterChain.doFilter(request, response);
        }


    }


    private String getAccessTokenFromHeader(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }


    private String getAccessToeknFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access")) {
                    String accessToekn = cookie.getValue();
                    log.info("accessToekn token: {}", accessToekn);
                    return accessToekn;
                }
            }
        }
        return null;
    }


    private String getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    String refreshToken = cookie.getValue();
                    return refreshToken;
                }
            }
        }
        return null;
    }


    private void setSecurityContext(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String accessToken) throws IOException, ServletException {
        log.info("인증완료 SecurityContextHolder 설정");
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
        return;
    }

    public void jwtExceptionHandler(HttpServletResponse response, BuiltInException error) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .process(error.getProcess())
                .build();

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
