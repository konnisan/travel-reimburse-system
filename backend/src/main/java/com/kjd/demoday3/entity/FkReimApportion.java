package com.kjd.demoday3.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("fk_reim_apportion")
public class FkReimApportion implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("main_id")
    private String mainId;
    @TableField("reim_company_id")
    private String reimCompanyId;
    @TableField("reim_company_no")
    private String reimCompanyNo;
    @TableField("reim_company_name")
    private String reimCompanyName;
    @TableField("project_id")
    private String projectId;
    @TableField("project_no")
    private String projectNo;
    @TableField("project_name")
    private String projectName;
    @TableField("apportion_ratio")
    private String apportionRatio;
    @TableField("apportion_amount")
    private String apportionAmount;
    @TableField("row_no")
    private Integer rowNo;
}
