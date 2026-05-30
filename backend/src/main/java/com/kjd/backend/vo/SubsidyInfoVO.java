package com.kjd.backend.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SubsidyInfoVO {
    private String subsidyId;
    private String tripId;
    private String travelerId;
    private String travelerNo;
    private String travelerName;
    private String startDate;
    private String endDate;
    private Integer subsidyDays;
    private String route;
    private String subsidyCityNo;
    private String subsidyCityName;
    private BigDecimal applyAmount;
    private BigDecimal subsidyAmount;
    private BigDecimal mealSubsidyAmount;
    private BigDecimal trafficSubsidyAmount;
    private BigDecimal communicationSubsidyAmount;
    private List<SubsidyCalendarVO> calendarList;
}
