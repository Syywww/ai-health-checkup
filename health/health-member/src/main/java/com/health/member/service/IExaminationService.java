package com.health.member.service;

import com.health.member.domain.ExaminationRecord;
import com.health.member.domain.ExaminationDetail;
import java.util.List;

public interface IExaminationService {
    List<ExaminationRecord> selectRecordList(ExaminationRecord record);
    ExaminationRecord selectRecordById(Long id);
    int insertRecord(ExaminationRecord record, List<ExaminationDetail> details);
    int updateRecord(ExaminationRecord record, List<ExaminationDetail> details);
    int deleteRecordById(Long id);
    int deleteRecordByIds(Long[] ids);
    int countRecordByDate(String startDate, String endDate);
}