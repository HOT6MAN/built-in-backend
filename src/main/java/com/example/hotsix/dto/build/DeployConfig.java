package com.example.hotsix.dto.build;

import lombok.*;
import lombok.Builder;
import org.springframework.web.bind.annotation.PathVariable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeployConfig {
    private Long memberId;
    private Long teamProjectInfoId;
    private Long deployNum;
    private Long serviceNum;
    private String jobType;
    private String accessToken;

    @Override
    public String toString() {
        return "DeployConfig{" +
                "memberId=" + memberId +
                ", teamProjectInfoId=" + teamProjectInfoId +
                ", deployNum=" + deployNum +
                ", serviceNum=" + serviceNum +
                ", jobType='" + jobType + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
