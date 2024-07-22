package com.example.hotsix.jwt;

import com.example.hotsix.oauth.dto.CustomOAuth2User;
import com.example.hotsix.oauth.dto.UserDTO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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



        String authorization = null;
        //쿠키들을 불러온뒤 Authorizaiton key에 담긴 쿠키를 찾음
        Cookie[] cookies = request.getCookies();
        log.info("cookies: {}", cookies);
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("access")) {
                    authorization = cookie.getValue();
                    break;
                }
            }
        }

        // Authorizaion헤더에서 access토큰 얻어오기
        String authorizationHeader = request.getHeader("Authorization");
        log.info("authorizationHeader: {}", authorizationHeader);
        if(authorizationHeader!=null && redisTemplate.hasKey(authorizationHeader)) {
            log.info("로그아웃된 access token");
            filterChain.doFilter(request, response);
            return;
        }


        if(authorizationHeader != null){
            authorization = authorizationHeader;
        }


        //Authorization 헤더 검증
        if(authorization == null){
            log.info("token is empty");
            filterChain.doFilter(request, response);
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //토큰
        String token = authorization;

        //토큰 소멸 시간 검증
        try{
            jwtUtil.isExpired(token) ;
        }catch (ExpiredJwtException e){
            //response body
            log.info("Access token is expired");
            PrintWriter writer = response.getWriter();
            writer.println("access toekn is expired");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //토큰이 acceess인지 확인
        String category = jwtUtil.getCategory(token);

        if(!category.equals("access")){
            log.info("Access token is invalid");
            PrintWriter writer = response.getWriter();
            writer.println("invalid access token");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }


        //토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        //userDTO를 생성하여 값 set
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setRole(role);

        //UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        //스프링 시큐리티 인증 토큰 생성

        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }
}
