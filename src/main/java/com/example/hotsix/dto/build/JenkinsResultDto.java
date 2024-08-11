package com.example.hotsix.dto.build;

import lombok.*;
import lombok.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JenkinsResultDto {
    private String serviceNum;
    private String memberId;
    private String teamProjectInfoId;
    private String buildNumber;;
    private String jobName;
    private String result;
    private String jobType;

    @Override
    public String toString() {
        return "JenkinsResultDto{" +
                "serviceNum='" + serviceNum + '\'' +
                ", memberId='" + memberId + '\'' +
                ", teamProjectInfoId='" + teamProjectInfoId + '\'' +
                ", buildNumber='" + buildNumber + '\'' +
                ", jobName='" + jobName + '\'' +
                ", result='" + result + '\'' +
                ", jobType='" + jobType + '\'' +
                '}';
    }
}
