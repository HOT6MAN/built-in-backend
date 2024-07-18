package com.example.hotsix.dto;

public interface OAuth2Response {

    // 제공자(naver, google, github...)
    String getProvider();

    //제공자가 발급해주는 id(번호)
    String getProviderId();

    //이메일
    String getEmail();

    //사용자 실명(설정한 이름)
    String getName();



}
