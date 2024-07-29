package com.example.hotsix.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final RedisTemplate<String, String> redisTemplate;
    private final JWTUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain );
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.info("로그아웃 필터");
        log.info("request url: {}", request.getRequestURI());
        String requestURI = request.getRequestURI();
        if(!requestURI.matches("^\\/logout$") || requestURI.startsWith("/member/")){
            System.out.println("member call");
            filterChain.doFilter(request, response);
            return;
        }
        String method = request.getMethod();
        if(!method.equals("POST")){
            filterChain.doFilter(request, response);
            return;
        }

        
        //  access토큰 redis에 블랙리스트 처리
        String access = null;
        access = request.getHeader("Authorization");
        log.info("access: {}", access);
        if(access != null){
            log.info("access토큰 블랙리스트 처리");
            long remainingTime = jwtUtil.getRemainingTime(access);
            log.info("remainingTime: {}", remainingTime);
            redisTemplate.opsForValue().set(access,"logout", remainingTime, TimeUnit.MILLISECONDS);
        }


        String refresh = null;
        Cookie[] cookies = request.getCookies();
//        log.info("cookies: {}", cookies);
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("refresh")){
                log.info("refresh cookies: {}", cookie.getName());
                refresh = cookie.getValue();

            }
        }

        if(refresh==null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String value = redisTemplate.opsForValue().get(jwtUtil.getUsername(refresh));
        if(value == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 리프레시 토큰 삭제
        log.info("Redis refresh토큰 삭제");
        redisTemplate.delete(jwtUtil.getUsername(refresh));

        //리프레시 토큰 쿠키 값 0
        Cookie cookie = new Cookie("refresh",null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        log.info("로그아웃 완료");
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
