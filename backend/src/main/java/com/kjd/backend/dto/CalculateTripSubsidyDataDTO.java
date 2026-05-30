package com.kjd.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CalculateTripSubsidyDataDTO extends ManualTripDTO {
    private String reimburseId;
}
