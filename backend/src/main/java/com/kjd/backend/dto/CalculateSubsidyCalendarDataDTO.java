package com.kjd.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class CalculateSubsidyCalendarDataDTO {
    private String reimburseId;
    private String subsidyId;
    private List<SubsidyCalendarDTO> calendarList;
}
