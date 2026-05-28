package com.kjd.demoday3.vo;

import lombok.Data;

import java.util.List;

@Data
public class TravelReimburseBaseDataVO {
    private List<ReimCompanyVO> reimCompanyList;
    private List<ReimDepartmentVO> departmentList;
    private List<EmployeeVO> employeeList;
    private List<BusinessTypeVO> businessTypeList;
    private List<CityVO> cityList;
    private List<ProjectVO> projectList;
}
