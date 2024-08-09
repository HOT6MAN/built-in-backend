package com.example.hotsix.jwt;

import com.example.hotsix.dto.common.APIResponse;
import com.example.hotsix.dto.common.ErrorResponse;
import com.example.hotsix.dto.common.ProcessResponse;
import com.example.hotsix.enums.Process;
import com.example.hotsix.exception.BuiltInException;
import com.example.hotsix.service.auth.LogoutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
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
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final LogoutService logoutService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain );
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.info("로그아웃 필터");
        log.info("request url: {}", request.getRequestURI());
        String requestURI = request.getRequestURI();
        if(!requestURI.matches("^/.*logout$")){
            log.info("/logout 요청이 아님");
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
            try{
                long remainingTime = jwtUtil.getRemainingTime(access);
                logoutService.blacklistAccessToken(access, remainingTime);
                //redisTemplate.opsForValue().set(access,"logout", remainingTime, TimeUnit.MILLISECONDS);
            }catch (ExpiredJwtException e){
                //response body
                log.info("Access 토큰 만료");

//                PrintWriter writer = response.getWriter();
//                writer.println("access toekn is expired");
//                response.setStatus(HttpServletResponse.SC_OK);
                BuiltInException exception = new BuiltInException(Process.NORMAL_RESPONSE);
                jwtExceptionHandler(response,exception);
                return;
            }catch( IllegalArgumentException e){
                BuiltInException exception = new BuiltInException(Process.NORMAL_RESPONSE);
                jwtExceptionHandler(response,exception);
                return;
            }


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
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        if(!logoutService.isTokenInRedis(jwtUtil.getId(refresh).toString())){
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // 리프레시 토큰 삭제
        log.info("Redis refresh토큰 삭제");
        logoutService.deleteRefreshToken(jwtUtil.getId(refresh).toString());

        //리프레시 토큰 쿠키 값 0
        Cookie cookie = new Cookie("refresh",null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        log.info("로그아웃 완료");
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void jwtExceptionHandler(HttpServletResponse response, BuiltInException error) {
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
