package com.example.hotsix.model;

import com.example.hotsix.model.project.DatabaseConfig;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "database_config_sql")
public class DatabaseConfigSql {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "origin_name")
    private String originName;
    @Column(name = "fixed_name")
    private String fixedName;
    @Column(name = "save_folder")
    private String saveFolder;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "database_config_id", referencedColumnName = "id")
    private DatabaseConfig databaseConfig;
}