package com.kjd.backend.vo;

import lombok.Data;

@Data
public class CreateTravelReimburseDraftVO {
    private String id;
    private Integer version;
    private String reimBillNo;
    private String billDate;
    private String billStatus;
    private String billStatusName;
    private String redirectUrl;
}
