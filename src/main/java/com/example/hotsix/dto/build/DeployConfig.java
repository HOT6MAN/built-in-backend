package com.example.hotsix.dto.build;

import com.example.hotsix.enums.JenkinsJobType;
import lombok.*;
import lombok.Builder;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

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
    private JenkinsJobType jobType;
    private String accessToken;
    private List<BackendConfigDto> backendConfigs;
    private List<FrontendConfigDto> frontendConfigs;
    private List<DatabaseConfigDto> databaseConfigs;

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
