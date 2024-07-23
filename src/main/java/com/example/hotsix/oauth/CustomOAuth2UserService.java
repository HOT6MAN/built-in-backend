package com.example.hotsix.oauth;

import com.example.hotsix.model.Member;
import com.example.hotsix.oauth.dto.*;

import com.example.hotsix.repository.member.MemberRepository;
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
        log.info("[CustomOAuth2UserService loadUser]");

        
        //리소스 서버로 부터 받을 유저정보
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oAuth2User: {}", oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("registrationId: {}", registrationId);

        OAuth2Response oAuth2Response = null;


        if(registrationId.equals("naver")){
            log.info("naver 로그인");
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }else if(registrationId.equals("google")){
            log.info("google 로그인");
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }else{

            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값

        log.info("oAuth2Response: {}", oAuth2Response);
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        String email = oAuth2Response.getEmail();
        Member existData = memberRepository.findByEmail(email);

        log.info("existData: {}", existData);
        // 가입한 유저 email이 없을 경우 email DB에 등록하고 로그인
        if(existData == null){
            log.info("신규 가입 이메일");
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
        }
        //이미 가입한 이메일
        else{
            log.info("이미 가입한 이메일");
            //직접 이메일로 가입한 경우
            if(existData.getLgnMtd().equals("builtin")){
                log.info("이메일 가입하기로 등록된 이메일");
                return null;
            }
            // oauth로 가입한 경우 바로 로그인 진행
            else{
                log.info("OAuth 가입하기로 등록된 이메일");
                UserDTO userDTO = new UserDTO();
                userDTO.setUsername(username);
                userDTO.setName(oAuth2Response.getName());
                userDTO.setRole("ROLE_USER");

                return new CustomOAuth2User(userDTO);

            }


        }

    }
}
