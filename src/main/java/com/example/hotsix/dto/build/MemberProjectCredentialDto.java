package com.example.hotsix.dto.build;

import com.example.hotsix.model.Member;
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
public class MemberProjectCredentialDto {
    private Long id;
    private Member member;
    private String jobName;
    private String gitUsername;
    private String gitToken;
    private String dockerUsername;
    private String dockerToken;
    private String gitCredentialId;
    private String dockerCredentialId;
}
