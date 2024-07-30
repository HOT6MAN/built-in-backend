package com.example.hotsix.service.auth;

import com.example.hotsix.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailLinkService {

    private final JWTUtil jwtUtil;

    public String createLink(String type, String email){
        String code = jwtUtil.createEmailJwt(email);

        String link = "http://localhost:8080/"+ type + "?code="+code;

        return link;
    }




}
