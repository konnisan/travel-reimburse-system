package com.kjd.demoday3.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CalculateSubsidyCalendarVO {
    private Boolean valid;
    private String message;
    private SubsidyInfoVO subsidyInfo;
    private BigDecimal totalSubsidyAmount;
    private BigDecimal mealSubsidyAmount;
    private BigDecimal trafficSubsidyAmount;
    private BigDecimal communicationSubsidyAmount;
}
