package com.health.member.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ExaminationDetail {
    private Long id;
    private Long recordId;
    private Long checkitemId;
    private String resultValue;
    private String unit;
    private String referenceRange;
    private String status;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;

    // 非数据库字段
    private String checkitemName;
}