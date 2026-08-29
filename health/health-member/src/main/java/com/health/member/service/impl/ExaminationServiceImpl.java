package com.health.member.service.impl;

import com.health.member.domain.ExaminationRecord;
import com.health.member.domain.ExaminationDetail;
import com.health.member.mapper.ExaminationRecordMapper;
import com.health.member.mapper.ExaminationDetailMapper;
import com.health.member.service.IExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ExaminationServiceImpl implements IExaminationService {

    @Autowired
    private ExaminationRecordMapper recordMapper;
    @Autowired
    private ExaminationDetailMapper detailMapper;

    @Override
    public List<ExaminationRecord> selectRecordList(ExaminationRecord record) {
        return recordMapper.selectRecordList(record);
    }

    @Override
    public ExaminationRecord selectRecordById(Long id) {
        ExaminationRecord record = recordMapper.selectRecordById(id);
        if (record != null) {
            ExaminationDetail detail = new ExaminationDetail();
            detail.setRecordId(id);
            record.setDetails(detailMapper.selectDetailList(detail));
        }
        return record;
    }

    @Override
    @Transactional
    public int insertRecord(ExaminationRecord record, List<ExaminationDetail> details) {
        int rows = recordMapper.insertRecord(record);
        if (details != null && !details.isEmpty()) {
            for (ExaminationDetail d : details) {
                d.setRecordId(record.getId());
            }
            detailMapper.batchInsertDetails(details);
        }
        return rows;
    }

    @Override
    @Transactional
    public int updateRecord(ExaminationRecord record, List<ExaminationDetail> details) {
        int rows = recordMapper.updateRecord(record);
        // 先删除旧明细，再插入新明细
        detailMapper.deleteDetailByRecordId(record.getId());
        if (details != null && !details.isEmpty()) {
            for (ExaminationDetail d : details) {
                d.setRecordId(record.getId());
            }
            detailMapper.batchInsertDetails(details);
        }
        return rows;
    }

    @Override
    @Transactional
    public int deleteRecordById(Long id) {
        // 级联删除由外键自动处理，但若没有外键则手动删除明细
        detailMapper.deleteDetailByRecordId(id);
        return recordMapper.deleteRecordById(id);
    }

    @Override
    @Transactional
    public int deleteRecordByIds(Long[] ids) {
        for (Long id : ids) {
            detailMapper.deleteDetailByRecordId(id);
        }
        return recordMapper.deleteRecordByIds(ids);
    }

    @Override
    public int countRecordByDate(String startDate, String endDate) {
        return recordMapper.countRecordByDate(startDate, endDate);
    }
}