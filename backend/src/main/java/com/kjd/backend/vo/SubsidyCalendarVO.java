package com.kjd.backend.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubsidyCalendarVO {
    private String calendarId;
    private String travelDate;
    private String weekName;
    private String cityNo;
    private String cityName;
    private String cityType;
    private Boolean mealChecked;
    private BigDecimal mealStandardAmount;
    private BigDecimal mealAmount;
    private Boolean trafficChecked;
    private BigDecimal trafficStandardAmount;
    private BigDecimal trafficAmount;
    private Boolean communicationChecked;
    private BigDecimal communicationStandardAmount;
    private BigDecimal communicationAmount;
}
