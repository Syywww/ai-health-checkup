package com.health.member.mapper;

import com.health.member.domain.ExaminationRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ExaminationRecordMapper {
    List<ExaminationRecord> selectRecordList(ExaminationRecord record);
    ExaminationRecord selectRecordById(Long id);
    int insertRecord(ExaminationRecord record);
    int updateRecord(ExaminationRecord record);
    int deleteRecordById(Long id);
    int deleteRecordByIds(Long[] ids);
    // 统计体检人数
    int countRecordByDate(@Param("startDate") String startDate, @Param("endDate") String endDate);
}