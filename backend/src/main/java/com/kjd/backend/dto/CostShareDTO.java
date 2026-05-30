package com.kjd.backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CostShareDTO {
    private String shareId;
    private Integer lineNo;
    private String reimCompanyId;
    private String reimCompanyNo;
    private String reimCompanyName;
    private String projectId;
    private String projectNo;
    private String projectName;
    private BigDecimal shareRatio;
    private BigDecimal shareAmount;
}
