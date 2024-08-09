package com.example.hotsix.model.id;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ApplyId implements Serializable {
    private Long teamId;
    private Long resumeId;
}
