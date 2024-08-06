package com.example.hotsix.controller.auth;

import com.example.hotsix.dto.member.MemberDto;
import com.example.hotsix.jwt.JWTUtil;
import com.example.hotsix.model.Member;
import com.example.hotsix.repository.member.MemberRepository;
import com.example.hotsix.service.auth.LoginService;
import com.example.hotsix.service.auth.LogoutService;
import com.example.hotsix.service.auth.MailLinkService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EmailController {

    private final MailLinkService mailLinkService;
    private final MemberRepository memberRepository;
    private final LoginService loginService;
    private final JWTUtil jwtUtil;
    private final LogoutService logoutService;

    @Value("${client.host}")
    private String clinetHost;

    // 링크를 생성하고 메일로 보내줌
    @PostMapping("/email-link")
    public String emailLink(@RequestBody String email) {
        log.info(email);
        Member exist = memberRepository.findByEmail(email.replace("\"", ""));
        log.info("exist: {}", exist);
        String link = null;
        //이미 존재하는 이메일
        if(exist != null) {
            link = mailLinkService.createLink("email-login", email);
            mailLinkService.sendMail(email, "login", link);
        }else{
            link = mailLinkService.createLink("register", email);
            mailLinkService.sendMail(email, "register", link);
        }
        //mailLinkService.sendMail(email, "login", link);
        return link;
    }


    // 메일로 온 링크로 들어오면 token검증하고 유효하면 로그인 처리
    @GetMapping("/email-login")
    public void emailLogin(@RequestParam("code") String code, @CookieValue(value = "refresh", required = false) Cookie refresh, HttpServletResponse response) throws IOException {
        String email = null;
        try{
            if(!isTokenExpired(code)){
                email = jwtUtil.getEmail(code);
            }

        }catch (JwtException e){
            log.info(e.getMessage());
            try {
                if(refresh!=null && logoutService.isTokenInRedis(jwtUtil.getId(refresh.getValue()).toString())){
                    // 리프레시 토큰 삭제
                    log.info("Redis refresh토큰 삭제");
                    logoutService.deleteRefreshToken(jwtUtil.getId(refresh.getValue()).toString());

                    //리프레시 토큰 쿠키 값 0
                    Cookie cookie = new Cookie("refresh",null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");

                    log.info("로그아웃 완료");
                    response.addCookie(cookie);
                    response.setStatus(HttpServletResponse.SC_OK);
                }

                response.sendRedirect(clinetHost);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        log.info("email {}", email);
        if(email != null) {

        Map<String, Cookie> cookies = loginService.login(email);

        response.addCookie(cookies.get("access"));
        response.addCookie(cookies.get("refresh"));
        response.sendRedirect(clinetHost+"/hot6man/afterlogin");
        }
    }

    //메일로 온 링크로 들어와서 토큰이 유요하면 프로필 작성 페이지로 리다이렉트
    @GetMapping("/register")
    public void register(@RequestParam("code") String code, HttpServletResponse response){
        log.info("code {}",code);

        try{
            if(!isTokenExpired(code)){
                String email = jwtUtil.getEmail(code);
                log.info("유효한 토큰 가입페이지로");
                response.sendRedirect(clinetHost + "/hot6man/register?email=" + email.replace("\"", ""));
            }

        }catch (JwtException | IOException e){
            log.info(e.getMessage());
            try {
                response.sendRedirect(clinetHost);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    // 프로필 작성 후 가입 버튼 누를시 가입완료
    @PostMapping("/signup")
    public MemberDto signup(@RequestBody Member member){
        log.info("member {}",member.toString());
        MemberDto memberDto = loginService.signUp(member);
        return memberDto;
    }


    private Boolean isTokenExpired(String code) {
        return jwtUtil.isExpired(code);
    }


}
