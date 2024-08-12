package com.example.hotsix.dto.apply;

import java.time.LocalDate;

public record ApplyShortResponse(
        Long resumeId,
        LocalDate date, // createdDate
        String applicant,
        String position,
        String status
) { }
