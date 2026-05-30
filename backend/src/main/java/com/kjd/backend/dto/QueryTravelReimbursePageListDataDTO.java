package com.kjd.backend.dto;

import lombok.Data;

@Data
public class QueryTravelReimbursePageListDataDTO {
    private String reimBillNo;
    private String title;
    private String reason;
    private String reimCompanyId;
    private String reimDepartmentId;
    private String reimburserId;
    private String businessTypeId;
    private String billStatus;
}
