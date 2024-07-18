package com.example.hotsix.service;

import com.example.hotsix.dto.*;
import com.example.hotsix.model.Member;
import com.example.hotsix.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        
        //리소스 서버로 부터 받을 유저정보
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oAuth2User: {}", oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("registrationId: {}", registrationId);

        OAuth2Response oAuth2Response = null;


        if(registrationId.equals("naver")){
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }else if(registrationId.equals("google")){
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }else{

            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값

        log.info("oAuth2Response: {}", oAuth2Response);
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        String email = oAuth2Response.getEmail();
        Member existData = memberRepository.findByEmail(email);


        // 가입한 유저 email이 없을 경우 email DB에 등록
        if(existData == null){
            Member member = new Member();
            member.setEmail(email);
            member.setNickname(oAuth2Response.getName());
            member.setRole("ROLE_USER");
            member.setLgnMtd(oAuth2Response.getProvider());

            memberRepository.save(member);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole("ROLE_USER");


            return new CustomOAuth2User(userDTO);
        }else{

            if(existData.getLgnMtd().equals("builtin")){
                return null;
            }else{
                UserDTO userDTO = new UserDTO();
                userDTO.setUsername(username);
                userDTO.setName(oAuth2Response.getName());
                userDTO.setRole("ROLE_USER");

                return new CustomOAuth2User(userDTO);

            }


        }

    }
}
