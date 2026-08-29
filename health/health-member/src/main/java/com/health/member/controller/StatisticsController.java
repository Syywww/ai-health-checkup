package com.health.member.controller;

import com.health.common.core.controller.BaseController;
import com.health.common.core.domain.AjaxResult;
import com.health.member.service.IMemberService;
import com.health.member.service.IExaminationService;
import com.health.member.vo.StatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member/statistics")
public class StatisticsController extends BaseController {

    @Autowired
    private IMemberService memberService;
    @Autowired
    private IExaminationService examinationService;

    @GetMapping
    public AjaxResult getStatistics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        StatisticsVO vo = new StatisticsVO();
        vo.setTotalMembers(memberService.countMemberByDate(startDate, endDate));
        vo.setTotalRecords(examinationService.countRecordByDate(startDate, endDate));
        // 可继续扩展其他统计，例如异常人数等
        return success(vo);
    }
}