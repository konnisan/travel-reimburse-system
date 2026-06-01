package com.kjd.backend.dto;

import lombok.Data;

@Data
public class InvalidTravelReimburseDataDTO {
    private String id;
    private Integer version;
    private String invalidReason;
}
