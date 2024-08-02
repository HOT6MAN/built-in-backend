package com.example.hotsix.dto.build;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@lombok.Builder
public class JobConfig {
    private String name;
    private String type;
    private SCM scm;
    private List<Builder> builders = new ArrayList<>();
    private List<Credential> credentials = new ArrayList<>();
}
