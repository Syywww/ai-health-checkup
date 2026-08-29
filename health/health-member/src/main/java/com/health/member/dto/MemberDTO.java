package com.health.member.dto;

import lombok.Data;
import java.util.Date;

@Data
public class MemberDTO {
    private Long id;
    private String name;
    private String gender;
    private Date birthday;
    private String phone;
    private String idCard;
    private String address;
    private String status;
    private String remark;
}