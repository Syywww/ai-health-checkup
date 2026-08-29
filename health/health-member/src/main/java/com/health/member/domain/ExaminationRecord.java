package com.health.member.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class ExaminationRecord {
    private Long id;
    private Long memberId;
    private Long setmealId;
    private Date examinationDate;
    private String doctor;
    private String resultSummary;
    private String status;
    private String attachment;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String updateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String remark;

    // 非数据库字段，用于关联查询展示
    private String memberName;
    private String setmealName;
    private List<ExaminationDetail> details;
}