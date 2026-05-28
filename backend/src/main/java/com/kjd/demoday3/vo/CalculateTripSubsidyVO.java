package com.kjd.demoday3.vo;

import lombok.Data;

import java.util.List;

@Data
public class CalculateTripSubsidyVO {
    private Boolean valid;
    private String message;
    private ManualTripVO tripInfo;
    private SubsidyInfoVO subsidyInfo;
    private List<SubsidyCalendarVO> calendarList;
}
