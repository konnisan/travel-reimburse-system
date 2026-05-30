package com.kjd.backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CalculateCostShareDataDTO {
    private String reimburseId;
    private String operateType;
    private BigDecimal totalShareAmount;
    private List<CostShareDTO> shareList;
}
