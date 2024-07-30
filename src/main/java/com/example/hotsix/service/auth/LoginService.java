package com.example.hotsix.service.auth;

import com.example.hotsix.dto.MemberDto;
import com.example.hotsix.jwt.JWTUtil;
import com.example.hotsix.model.Member;
import com.example.hotsix.repository.member.MemberRepository;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    @Value("${jwt.access-token.expiretime}")
    private Long accessExpiretime;

    @Value("${jwt.refresh-token.expiretime}")
    private Long refreshExpiretime;

    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;
    private final RedisTemplate<String ,String> redisTemplate;

    public MemberDto signUp(Member member) {
        member.setRole("ROLE_USER");
        member.setLgnMtd("built-in");
        memberRepository.save(member);

        MemberDto memberDto = MemberDto.builder()
                .id(member.getId())
                .name(member.getNickname())
                .phone(member.getPhone())
                .build();

        return memberDto;
    }

    public Map<String, Cookie> login(String email) {
        Member member = memberRepository.findByEmail(email);

        MemberDto memberDto = MemberDto.builder()
                .id(member.getId())
                .name(member.getNickname())
                .role(member.getRole())
                .build();


        //JWT 생성
        String accessToken = jwtUtil.createAccessToken(memberDto, accessExpiretime);
        String refreshToken = jwtUtil.createRefreshToken(memberDto, refreshExpiretime);

        //리프레시토큰 저장
        redisTemplate.opsForValue().set(member.getId().toString(), refreshToken, refreshExpiretime, TimeUnit.MILLISECONDS);

        Map<String, Cookie> map = new HashMap<>();
        Cookie access = createCookie("access", accessToken, accessExpiretime);
        Cookie refresh = createCookie("refresh", refreshToken, refreshExpiretime);
        map.put("access",access);
        map.put("refresh",refresh);
        return map;
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
