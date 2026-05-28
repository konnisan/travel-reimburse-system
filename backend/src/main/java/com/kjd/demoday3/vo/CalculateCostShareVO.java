package com.kjd.demoday3.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CalculateCostShareVO {
    private Boolean valid;
    private String message;
    private List<CostShareVO> shareList;
    private BigDecimal totalShareRatio;
    private BigDecimal totalShareAmount;
}
