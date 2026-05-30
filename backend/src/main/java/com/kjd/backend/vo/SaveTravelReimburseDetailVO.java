package com.kjd.backend.vo;

import lombok.Data;

@Data
public class SaveTravelReimburseDetailVO {
    private Boolean valid;
    private String message;
    private TravelReimburseDetailVO detail;
}
