package com.example.hotsix.model.project;

import com.example.hotsix.dto.build.DatabaseConfigDto;
import com.example.hotsix.enums.DatabaseJobName;
import com.example.hotsix.enums.FrontendJobName;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

@Entity(name = "database_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatabaseConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_info_id")
    private TeamProjectInfo projectInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "database_job_name")
    private DatabaseJobName databaseJobName;

    @Column(name = "config_name")
    private String configName;

    @Column(name = "url")
    private String url;

    @Column(name = "schema_name")
    private String schemaName;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    public DatabaseConfigDto toDto() {
        return DatabaseConfigDto.builder()
                .id(id)
                .configName(configName)
                .url(url)
                .schemaName(schemaName)
                .username(username)
                .password(password)
                .build();
    }

    public static DatabaseConfig fromDto(DatabaseConfigDto dto) {
        return DatabaseConfig.builder()
                .id(dto.getId())
                .configName(dto.getConfigName())
                .url(dto.getUrl())
                .schemaName(dto.getSchemaName())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build();
    }

    public void setProperties(DatabaseConfigDto dto) {
        this.url = dto.getUrl();
        this.configName = dto.getConfigName();
        this.schemaName = dto.getSchemaName();
        this.username = dto.getUsername();
        this.password = dto.getPassword();
    }
}

