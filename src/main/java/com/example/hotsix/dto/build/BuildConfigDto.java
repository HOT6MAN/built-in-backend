package com.example.hotsix.dto.build;

import lombok.*;
import lombok.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuildConfigDto {
    private Long serviceNum;
    private Long projectInfoId;
    private String gitBackUrl;
    private String gitBackBranch;
    private String gitBackUsername;
    private String gitBackAccessToken;
    private String gitFrontUrl;
    private String gitFrontBranch;
    private String gitFrontUsername;
    private String gitFrontAccessToken;
    private String databaseUrl;
    private String databaseUsername;
    private String databasePassword;
}
