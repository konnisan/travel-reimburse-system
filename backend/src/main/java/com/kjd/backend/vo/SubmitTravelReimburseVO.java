package com.kjd.backend.vo;

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
