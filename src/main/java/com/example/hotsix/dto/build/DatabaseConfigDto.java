package com.example.hotsix.dto.build;

import lombok.*;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DatabaseConfigDto {
    private Long id;
    private Long teamProjectInfoId;
    private String configName;
    private String url;
    private String schemaName;
    private String username;
    private String password;
}
