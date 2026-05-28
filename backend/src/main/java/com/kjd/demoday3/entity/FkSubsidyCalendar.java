package com.kjd.demoday3.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("fk_subsidy_calendar")
public class FkSubsidyCalendar implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("main_id")
    private String mainId;
    @TableField("travel_date")
    private String travelDate;
    @TableField("travel_date_week")
    private String travelDateWeek;
    @TableField("subsidized_cities")
    private String subsidizedCities;
    @TableField("subsidized_city_number")
    private String subsidizedCityNumber;
    @TableField(exist = false)
    private String cityType;
    @TableField("standard_meal_expenses_amount")
    private String standardMealExpensesAmount;
    @TableField("standard_traffic_amount")
    private String standardTrafficAmount;
    @TableField("standard_communication_amount")
    private String standardCommunicationAmount;
    @TableField("meal_expenses_amount")
    private String mealExpensesAmount;
    @TableField("traffic_amount")
    private String trafficAmount;
    @TableField("communication_amount")
    private String communicationAmount;
    @TableField("is_reimbursed")
    private String isReimbursed;
    @TableField(exist = false)
    private String mealChecked;
    @TableField(exist = false)
    private String trafficChecked;
    @TableField(exist = false)
    private String communicationChecked;
    @TableField("remark")
    private String remark;
}
