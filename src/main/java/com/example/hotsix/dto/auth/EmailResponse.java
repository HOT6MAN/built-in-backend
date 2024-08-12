package com.example.hotsix.dto.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailResponse {
    private String link;
    private String type;

}
