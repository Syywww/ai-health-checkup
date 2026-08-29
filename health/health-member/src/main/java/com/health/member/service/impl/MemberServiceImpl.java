package com.health.member.service.impl;

import com.health.member.domain.Member;
import com.health.member.mapper.MemberMapper;
import com.health.member.service.IMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MemberServiceImpl implements IMemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public List<Member> selectMemberList(Member member) {
        return memberMapper.selectMemberList(member);
    }

    @Override
    public Member selectMemberById(Long id) {
        return memberMapper.selectMemberById(id);
    }

    @Override
    public int insertMember(Member member) {
        return memberMapper.insertMember(member);
    }

    @Override
    public int updateMember(Member member) {
        return memberMapper.updateMember(member);
    }

    @Override
    public int deleteMemberById(Long id) {
        return memberMapper.deleteMemberById(id);
    }

    @Override
    public int deleteMemberByIds(Long[] ids) {
        return memberMapper.deleteMemberByIds(ids);
    }

    @Override
    public int countMemberByDate(String startDate, String endDate) {
        return memberMapper.countMemberByDate(startDate, endDate);
    }
}