package com.health.member.controller;

import com.health.common.core.controller.BaseController;
import com.health.common.core.domain.AjaxResult;
import com.health.common.core.page.TableDataInfo;
import com.health.member.domain.Member;
import com.health.member.service.IMemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member/member")
public class MemberController extends BaseController {

    private final IMemberService memberService;

    // 构造注入，消除字段注入警告
    public MemberController(IMemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/list")
    public TableDataInfo list(Member member) {
        startPage();
        List<Member> list = memberService.selectMemberList(member);
        return getDataTable(list);
    }

    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(memberService.selectMemberById(id));
    }

    @PostMapping
    public AjaxResult add(@RequestBody Member member) {
        member.setCreateBy(getUsername());
        return toAjax(memberService.insertMember(member));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody Member member) {
        member.setUpdateBy(getUsername());
        return toAjax(memberService.updateMember(member));
    }

    // 单删区分路径
    @DeleteMapping("/single/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(memberService.deleteMemberById(id));
    }

    // 批量删除区分路径，避免路径冲突
    @DeleteMapping("/batch/{ids}")
    public AjaxResult removeAll(@PathVariable Long[] ids) {
        return toAjax(memberService.deleteMemberByIds(ids));
    }
}