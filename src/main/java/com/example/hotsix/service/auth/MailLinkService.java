package com.example.hotsix.service.auth;

import com.example.hotsix.jwt.JWTUtil;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static org.springframework.security.core.context.SecurityContextHolder.setContext;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailLinkService {

    private final JWTUtil jwtUtil;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;


    public String createLink(String type, String email){
        String code = jwtUtil.createEmailJwt(email);
        log.info(code);
        String link = "http://localhost:8080/"+ type + "?code="+code;

        return link;
    }

    public void sendMail(String email, String type, String link){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject("Test"); // 메일 제목
            mimeMessageHelper.setText(setContext(type,link), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            log.info("Succeeded to send Email");
        } catch (Exception e) {
            log.info("Failed to send Email");
            throw new RuntimeException(e);
        }
    }

    //thymeleaf를 통한 html 적용
    public String setContext(String type, String link) {
        Context context = new Context();
        context.setVariable("link", link);
        return templateEngine.process(type, context);
    }


}
