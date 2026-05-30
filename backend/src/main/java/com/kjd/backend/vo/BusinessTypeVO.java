package com.kjd.backend.vo;

import lombok.Data;

@Data
public class BusinessTypeVO {

    /** 业务类型id */
    private String businessTypeId;

    /** 业务类型编号 */
    private String businessTypeNo;

    /** 业务类型名称 */
    private String businessTypeName;

    /** 是否有下级节点：1 有，0 无 */
    private String thereSubordinateNode;

    /** 上级业务类型id，none表示最上级 */
    private String superiorId;

}