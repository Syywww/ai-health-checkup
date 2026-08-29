package com.health.reservation.service;

import java.util.List;

import com.health.common.utils.bean.BeanUtils;
import com.health.reservation.domain.TCheckgroup;
import com.health.reservation.dto.TCheckgroupDto;
import com.health.reservation.mapper.TCheckgroupCheckitemMapper;
import com.health.reservation.mapper.TCheckgroupMapper;
import com.health.reservation.vo.TCheckgroupVo;
import org.springframework.transaction.annotation.Transactional;

/**
 * 检查组管理Service接口
 * 
 * @author ruoyi
 * @date 2026-06-30
 */
public interface ITCheckgroupService 
{
    /**
     * 查询检查组
     *
     * @param id 检查组主键
     * @return 检查组
     */
    public TCheckgroupVo selectTCheckgroupById(Long id);

    /**
     * 查询检查组管理列表
     * 
     * @param tCheckgroup 检查组管理
     * @return 检查组管理集合
     */
    public List<TCheckgroup> selectTCheckgroupList(TCheckgroup tCheckgroup);

    /**
     * 新增检查组管理
     * 
     * @param dto 检查组管理
     * @return 结果
     */
    public int insertTCheckgroup(TCheckgroupDto dto);
//    public int insertTCheckgroup(TCheckgroup tCheckgroup);

    /**
     * 修改检查组
     *
     * @param dto 检查组
     * @return 结果
     */
    public int updateTCheckgroup(TCheckgroupDto dto);

    /**
     * 批量删除检查组管理
     * 
     * @param ids 需要删除的检查组管理主键集合
     * @return 结果
     */
    public int deleteTCheckgroupByIds(Long[] ids);

    /**
     * 删除检查组信息
     *
     * @param id 检查组主键
     * @return 结果
     */
    public int deleteTCheckgroupById(Long id);
}
