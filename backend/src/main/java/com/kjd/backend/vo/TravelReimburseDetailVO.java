package com.kjd.backend.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TravelReimburseDetailVO {
    private String id;
    private String reimBillNo;
    private String billDate;
    private String billStatus;
    private String billStatusName;
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
    private BigDecimal totalSubsidyAmount;
    private BigDecimal mealSubsidyAmount;
    private BigDecimal trafficSubsidyAmount;
    private BigDecimal communicationSubsidyAmount;
    private String remark;
    private List<ManualTripVO> tripList;
    private List<SubsidyInfoVO> subsidyList;
    private List<CostShareVO> shareList;
}
