package com.example.hotsix.dto.build;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@lombok.Builder
public class Builder {
    private String type;
    private String command;
}
