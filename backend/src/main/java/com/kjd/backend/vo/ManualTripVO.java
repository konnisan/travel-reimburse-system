package com.kjd.backend.vo;

import lombok.Data;

@Data
public class ManualTripVO {
    private String tripId;
    private String travelerId;
    private String travelerNo;
    private String travelerName;
    private String departureCityNo;
    private String departureCityName;
    private String arrivalCityNo;
    private String arrivalCityName;
    private String departureDate;
    private String arrivalDate;
    private String tripDesc;
}
