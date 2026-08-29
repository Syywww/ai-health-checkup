package com.health.member.service;

import com.health.member.domain.Member;
import java.util.List;

public interface IMemberService {
    List<Member> selectMemberList(Member member);
    Member selectMemberById(Long id);
    int insertMember(Member member);
    int updateMember(Member member);
    int deleteMemberById(Long id);
    int deleteMemberByIds(Long[] ids);
    int countMemberByDate(String startDate, String endDate);
}