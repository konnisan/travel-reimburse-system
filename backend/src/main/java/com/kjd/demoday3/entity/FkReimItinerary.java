package com.kjd.demoday3.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("fk_reim_itinerary")
public class FkReimItinerary implements Serializable {
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
    @TableField("departure_city")
    private String departureCity;
    @TableField("departure_city_no")
    private String departureCityNo;
    @TableField("arriving_city")
    private String arrivingCity;
    @TableField("arriving_city_no")
    private String arrivingCityNo;
    @TableField("itinerary_instructions")
    private String itineraryInstructions;
}
