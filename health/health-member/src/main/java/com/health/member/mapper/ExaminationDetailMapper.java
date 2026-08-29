package com.health.member.mapper;

import com.health.member.domain.ExaminationDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ExaminationDetailMapper {
    List<ExaminationDetail> selectDetailList(ExaminationDetail detail);
    ExaminationDetail selectDetailById(Long id);
    int insertDetail(ExaminationDetail detail);
    int updateDetail(ExaminationDetail detail);
    int deleteDetailByRecordId(Long recordId);
    int deleteDetailById(Long id);
    // 批量插入
    int batchInsertDetails(List<ExaminationDetail> details);
}