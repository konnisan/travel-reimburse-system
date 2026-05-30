package com.kjd.backend.dto;

import lombok.Data;

@Data
public class QueryTravelReimbursePageListDTO {
    private Integer current = 1;
    private Integer size = 10;
    private QueryTravelReimbursePageListDataDTO data;
}
