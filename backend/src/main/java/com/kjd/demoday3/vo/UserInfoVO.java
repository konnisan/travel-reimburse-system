package com.kjd.demoday3.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserInfoVO {
    private String id;
    private String username;
    private String displayName;
    private List<String> roles;
    private List<String> permissions;
}
