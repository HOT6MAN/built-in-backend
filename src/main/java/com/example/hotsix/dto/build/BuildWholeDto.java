package com.example.hotsix.dto.build;

import lombok.*;
import lombok.Builder;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuildWholeDto {
    int totalCount;
    List<BuildResultInfoDto> buildResults;
}
