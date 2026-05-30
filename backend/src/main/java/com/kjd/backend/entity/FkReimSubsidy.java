package com.kjd.backend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("fk_reim_subsidy")
public class FkReimSubsidy implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("main_id")
    private String mainId;
    @TableField("traveler_id")
    private String travelerId;
    @TableField("traveler_no")
    private String travelerNo;
    @TableField("traveler_name")
    private String travelerName;
    @TableField("departure_date")
    private String departureDate;
    @TableField("arrival_date")
    private String arrivalDate;
    @TableField("subsidy_days")
    private String subsidyDays;
    @TableField("departure_city")
    private String departureCity;
    @TableField("departure_city_no")
    private String departureCityNo;
    @TableField("arriving_city")
    private String arrivingCity;
    @TableField("arriving_city_no")
    private String arrivingCityNo;
    @TableField("application_amount")
    private String applicationAmount;
    @TableField("subsidy_amount")
    private String subsidyAmount;
    @TableField("meal_allowance")
    private String mealAllowance;
    @TableField("transportation_allowance")
    private String transportationAllowance;
    @TableField("phone_allowance")
    private String phoneAllowance;
    @TableField("business_type_id")
    private String businessTypeId;
    @TableField("business_type_no")
    private String businessTypeNo;
    @TableField("business_type_name")
    private String businessTypeName;
}
