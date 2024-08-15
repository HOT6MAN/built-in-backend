package com.example.hotsix.dto.build;

import com.example.hotsix.model.project.DatabaseConfig;
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
    private String sqlFileName;

    public static DatabaseConfigDto fromEntity(DatabaseConfig entity) {
        DatabaseConfigDto dto = new DatabaseConfigDto();
        // 기존 필드 설정...

        if (entity.getDatabaseConfigSQL() != null) {
            dto.setSqlFileName(entity.getDatabaseConfigSQL().getOriginName());
        }

        return dto;
    }
}
