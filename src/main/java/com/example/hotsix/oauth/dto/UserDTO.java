package com.example.hotsix.oauth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private String role;
    //사용자 이름
    private String name;
    //사용자를 특정할 값
    private String username;

}
