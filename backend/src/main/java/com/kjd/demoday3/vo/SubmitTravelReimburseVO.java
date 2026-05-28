package com.kjd.demoday3.vo;

import lombok.Data;

@Data
public class SubmitTravelReimburseVO {
    private Boolean valid;
    private String message;
    private String id;
    private String reimBillNo;
    private String billStatus;
    private String billStatusName;
}
