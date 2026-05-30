package com.kjd.backend.vo;

import lombok.Data;

@Data
public class InvalidTravelReimburseVO {
    private String id;
    private String billStatus;
    private String billStatusName;
    private String message;
}
