package com.kjd.demoday3.dto;

import lombok.Data;

import java.util.List;

@Data
public class TravelReimburseDetailSaveDTO {
    private String id;
    private String billDate;
    private String title;
    private String reason;
    private String reimburserId;
    private String reimburserNo;
    private String reimburserName;
    private String reimDepartmentId;
    private String reimDepartmentNo;
    private String reimDepartmentName;
    private String reimCompanyId;
    private String reimCompanyNo;
    private String reimCompanyName;
    private String businessTypeId;
    private String businessTypeNo;
    private String businessTypeName;
    private List<ManualTripDTO> tripList;
    private List<SubsidyInfoDTO> subsidyList;
    private List<CostShareDTO> shareList;
    private String remark;
}
