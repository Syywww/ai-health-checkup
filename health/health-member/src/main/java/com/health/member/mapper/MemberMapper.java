package com.health.member.mapper;

import com.health.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MemberMapper {
    List<Member> selectMemberList(Member member);
    Member selectMemberById(Long id);
    int insertMember(Member member);
    int updateMember(Member member);
    int deleteMemberById(Long id);
    int deleteMemberByIds(Long[] ids);
    // 统计功能
    int countMemberByDate(@Param("startDate") String startDate, @Param("endDate") String endDate);
}