package com.kjd.backend.vo;

import lombok.Data;

@Data
public class CityVO {

    /** 城市编号 */
    private String cityNo;

    /** 城市名称 */
    private String cityName;

    /** 城市类型：1 一线，2 二线，3 三线 */
    private String cityType;

}