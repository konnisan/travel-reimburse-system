package com.kjd.demoday3.vo;

import lombok.Data;

@Data
public class SaveTravelReimburseDetailVO {
    private Boolean valid;
    private String message;
    private TravelReimburseDetailVO detail;
}
