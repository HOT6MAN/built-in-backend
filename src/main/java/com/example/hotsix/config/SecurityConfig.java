package com.example.hotsix.config;

import com.example.hotsix.jwt.CustomAuthenticationEntryPoint;
import com.example.hotsix.jwt.CustomLogoutFilter;
import com.example.hotsix.jwt.JWTFilter;
import com.example.hotsix.jwt.JWTUtil;
import com.example.hotsix.oauth.CustomSuccessHandler;
import com.example.hotsix.oauth.CustomOAuth2UserService;
import com.example.hotsix.service.auth.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    private final CustomSuccessHandler customSuccessHandler;

    private final JWTUtil jwtUtil;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    //private final RedisTemplate<String ,String> redisTemplate;
    private final LogoutService logoutService;

    @Value("${client.host}")
    private String clientHost;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CORS
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource()));

        // CSRF disable
        http
                .csrf((auth) -> auth.disable());

        // Form 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        // HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        // JWTFilter 추가   UsernamePasswordFilter 이전에 등록
        http
                .addFilterBefore(new JWTFilter(jwtUtil, logoutService), UsernamePasswordAuthenticationFilter.class);
        // 로그아웃
        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, logoutService), LogoutFilter.class);

        // oauth2
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)))
                        .successHandler(customSuccessHandler));

        //인증실패시
        http.
                exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint));


        // 경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/").permitAll()
                        .requestMatchers("/reissue").permitAll()
                        .requestMatchers("/convert").permitAll()
                        .requestMatchers("/email-link").permitAll()
                        .requestMatchers("/email-login").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/signup").permitAll()
                        .requestMatchers("/email-register").permitAll()
                        //.requestMatchers("/teams/**").permitAll()
                        .requestMatchers("/hot6man/test/**").permitAll()
                        .requestMatchers(("/member/**")).permitAll()
                        .requestMatchers(("/build/**")).permitAll()
                        .requestMatchers("/log/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/ws/log/**").permitAll()
                        .requestMatchers(("/hot6man/member/**")).permitAll()
                        .anyRequest().authenticated());

        // 세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Collections.singletonList(clientHost));
        configuration.setAllowedOrigins(Arrays.asList(clientHost, "http://localhost:5173"));
        configuration.setAllowedMethods(Collections.singletonList("*")); // GET, POST, PUT 등 모든 요청 허용
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Collections.singletonList("*")); // 받을 헤더값 세팅
        configuration.setMaxAge(3600L);
        configuration.addExposedHeader("Set-Cookie");
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
