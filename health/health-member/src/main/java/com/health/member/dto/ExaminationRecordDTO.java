package com.health.member.dto;

import com.health.member.domain.ExaminationDetail;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class ExaminationRecordDTO {
    private Long id;
    private Long memberId;
    private Long setmealId;
    private Date examinationDate;
    private String doctor;
    private String resultSummary;
    private String status;
    private String attachment;
    private String remark;

    // 关键：明确泛型为 ExaminationDetail
    private List<ExaminationDetail> details;
}