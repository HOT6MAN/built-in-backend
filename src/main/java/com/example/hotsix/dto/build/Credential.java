package com.example.hotsix.dto.build;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credential {
    private String username;
    private String password;
    private String id;
    private String description;
    private String scope;
}
