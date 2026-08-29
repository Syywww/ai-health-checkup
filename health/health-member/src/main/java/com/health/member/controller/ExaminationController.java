package com.health.member.controller;

import com.health.common.core.controller.BaseController;
import com.health.common.core.domain.AjaxResult;
import com.health.common.core.page.TableDataInfo;
import com.health.member.domain.ExaminationRecord;
import com.health.member.dto.ExaminationRecordDTO;
import com.health.member.service.IExaminationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member/examination")
public class ExaminationController extends BaseController {

    @Autowired
    private IExaminationService examinationService;

    @GetMapping("/list")
    public TableDataInfo list(ExaminationRecord record) {
        startPage();
        List<ExaminationRecord> list = examinationService.selectRecordList(record);
        return getDataTable(list);
    }

    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(examinationService.selectRecordById(id));
    }

    @PostMapping
    public AjaxResult add(@RequestBody ExaminationRecordDTO dto) {
        ExaminationRecord record = new ExaminationRecord();
        BeanUtils.copyProperties(dto, record);
        record.setCreateBy(getUsername());
        return toAjax(examinationService.insertRecord(record, dto.getDetails()));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody ExaminationRecordDTO dto) {
        ExaminationRecord record = new ExaminationRecord();
        BeanUtils.copyProperties(dto, record);
        record.setUpdateBy(getUsername());
        return toAjax(examinationService.updateRecord(record, dto.getDetails()));
    }

    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(examinationService.deleteRecordById(id));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult removeAll(@PathVariable Long[] ids) {
        return toAjax(examinationService.deleteRecordByIds(ids));
    }
}