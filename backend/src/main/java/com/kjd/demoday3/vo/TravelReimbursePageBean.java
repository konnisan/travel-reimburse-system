package com.kjd.demoday3.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TravelReimbursePageBean {
    private String id;
    private String reimBillNo;
    private String billStatus;
    private String billStatusName;
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
    private String businessTypeName;
    private String title;
    private String reason;
    private BigDecimal subsidyAmount;
    private Date createTime;
}
