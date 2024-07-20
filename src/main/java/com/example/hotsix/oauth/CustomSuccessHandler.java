package com.example.hotsix.oauth;

import com.example.hotsix.dto.CustomOAuth2User;
import com.example.hotsix.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${jwt.access-token.expiretime}")
    private Long accessExpiretime;

    @Value("${jwt.refresh-token.expiretime}")
    private Long refreshExpiretime;

    @Value("${client.host}")
    private String clinetHost;

    private final JWTUtil jwtUtil;

    private final RedisTemplate<String ,String> redisTemplate;




    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("[CustomSuccessHandler]");
        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        //JWT생성하기 위해 username, role값이 필요
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //JWT 생성
        String accessToken = jwtUtil.createAccessToken(username, role, accessExpiretime);
        String refreshToken = jwtUtil.createRefreshToken(username, role, refreshExpiretime);

        //리프레시토큰 저장
        redisTemplate.opsForValue().set(username, refreshToken, refreshExpiretime, TimeUnit.MILLISECONDS);

        response.addCookie(createCookie("access", accessToken, accessExpiretime));
        response.addCookie(createCookie("refresh", refreshToken, refreshExpiretime));
        response.sendRedirect(clinetHost);

    }

    private Cookie createCookie(String key, String value, Long expiretime){
        log.info("JWT 담을 쿠키 생성");
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiretime.intValue());
        //cookie.setSecure(true);   //https 프로토골
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

}
