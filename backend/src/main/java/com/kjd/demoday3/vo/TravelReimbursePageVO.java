package com.kjd.demoday3.vo;

import lombok.Data;

import java.util.List;

@Data
public class TravelReimbursePageVO {
    private Long total;
    private Long pages;
    private Long current;
    private Long size;
    private List<TravelReimbursePageBean> records;
}
