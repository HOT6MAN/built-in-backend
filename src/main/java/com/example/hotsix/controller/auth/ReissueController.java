
package com.example.hotsix.controller.auth;


import com.example.hotsix.dto.member.MemberDto;
import com.example.hotsix.enums.Process;
import com.example.hotsix.exception.BuiltInException;
import com.example.hotsix.jwt.JWTUtil;
import com.example.hotsix.service.auth.LogoutService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReissueController {


    private final JWTUtil jwtUtil;
    private final LogoutService logoutService;
    //private final RedisTemplate<String ,String> redisTemplate;

    @Value("${jwt.access-token.expiretime}")
    private Long accessExpiretime;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        log.info("reissue");
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("refresh")){
                refresh = cookie.getValue();
            }
        }

        if(refresh ==null){
            log.info("refresh is null");
            throw new BuiltInException(Process.INVALID_TOKEN);
            //return new ResponseEntity<>("refresh token is null", HttpStatus.BAD_REQUEST);
        }

        try {
            jwtUtil.isExpired(refresh);
        }catch (ExpiredJwtException e){
            log.info("refresh token is expired");
            throw new BuiltInException(Process.EXPIRED_TOKEN);
            //return new ResponseEntity<>("refresh token is expired", HttpStatus.BAD_REQUEST);
        }

        String category = jwtUtil.getCategory(refresh);

        if(!category.equals("refresh")){
            log.info("refresh token is invalid");
            throw new BuiltInException(Process.INVALID_TOKEN);
            //return new ResponseEntity<>("refresh token is invalid", HttpStatus.BAD_REQUEST);
        }

        // 레디스에 리프레시토큰없을때
        if( !logoutService.isTokenInRedis(jwtUtil.getId(refresh).toString())){
            log.info("만료된 리프레시토큰");
            throw new BuiltInException(Process.EXPIRED_TOKEN);
            //return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }


        log.info("Access토큰 재발행");
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);
        Long id= jwtUtil.getId(refresh);
        String name = jwtUtil.getName(refresh);
        String email = jwtUtil.getEmail(refresh);

        MemberDto memberDto = MemberDto.builder()
                .id(id)
                .email(email)
                .name(name)
                .role(role)
                .build();


        String newAccess = jwtUtil.createAccessToken(memberDto, accessExpiretime);

        //response.setHeader("access", newAccess);
        response.setHeader("Authorization", "Bearer " + newAccess);

        return new ResponseEntity<>(HttpStatus.OK);


    }


}