package com.kjd.demoday3.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("fk_reim_main")
public class FkReimMain implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("reim_bill_no")
    private String reimBillNo;
    @TableField("bill_status")
    private String billStatus;
    @TableField(exist = false)
    private String billDate;
    @TableField("creation_time")
    private String creationTime;
    @TableField("reimbursement_title")
    private String reimbursementTitle;
    @TableField("reimburser_id")
    private String reimburserId;
    @TableField("reimburser_no")
    private String reimburserNo;
    @TableField("reimburser_name")
    private String reimburserName;
    @TableField("reim_department_id")
    private String reimDepartmentId;
    @TableField("reim_department_no")
    private String reimDepartmentNo;
    @TableField("reim_department_name")
    private String reimDepartmentName;
    @TableField("reim_company_id")
    private String reimCompanyId;
    @TableField("reim_company_no")
    private String reimCompanyNo;
    @TableField("reim_company_name")
    private String reimCompanyName;
    @TableField("business_type_id")
    private String businessTypeId;
    @TableField("business_type_no")
    private String businessTypeNo;
    @TableField("business_type_name")
    private String businessTypeName;
    @TableField("business_trip_reason")
    private String businessTripReason;
    @TableField("subsidy_total")
    private String subsidyTotal;
    @TableField("meal_allowance")
    private String mealAllowance;
    @TableField("transportation_allowance")
    private String transportationAllowance;
    @TableField("phone_allowance")
    private String phoneAllowance;
    @TableField("remarks")
    private String remarks;
}
