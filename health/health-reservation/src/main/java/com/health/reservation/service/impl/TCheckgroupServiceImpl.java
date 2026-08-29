package com.health.reservation.service.impl;

import java.util.List;
import com.health.common.utils.DateUtils;
import com.health.common.utils.bean.BeanUtils;
import com.health.reservation.dto.TCheckgroupDto;
import com.health.reservation.mapper.TCheckgroupCheckitemMapper;
import com.health.reservation.mapper.TSetmealCheckgroupMapper;
import com.health.reservation.vo.TCheckgroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.health.reservation.mapper.TCheckgroupMapper;
import com.health.reservation.domain.TCheckgroup;
import com.health.reservation.service.ITCheckgroupService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 检查组管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-06-30
 */
@Transactional
@Service
public class TCheckgroupServiceImpl implements ITCheckgroupService 
{
    @Autowired
    private TCheckgroupMapper tCheckgroupMapper;
    @Autowired
    private TCheckgroupCheckitemMapper tCheckgroupCheckitemMapper;
    @Autowired
    private TSetmealCheckgroupMapper tSetmealCheckgroupMapper;

    /**
     * 查询检查组管理
     *
     * @param id 检查组管理主键
     * @return 检查组管理
     */
    @Override
    public TCheckgroupVo selectTCheckgroupById(Long id)
    {
        //1.查询检查组基本信息
        TCheckgroup tCheckgroup = tCheckgroupMapper.selectTCheckgroupById(id);
        //2.根据检查组的id查询检查项关联数据
        List<Long> list=tCheckgroupCheckitemMapper.selectCheckitemIds(id);
        //3.封装vo对象
        TCheckgroupVo vo=new TCheckgroupVo();
        BeanUtils.copyProperties(tCheckgroup,vo);
        vo.setCheckItemIds(list);
        return vo;
    }
//    @Override
//    public TCheckgroup selectTCheckgroupById(Long id)
//    {
//        return tCheckgroupMapper.selectTCheckgroupById(id);
//    }

    /**
     * 查询检查组管理列表
     * 
     * @param tCheckgroup 检查组管理
     * @return 检查组管理
     */
    @Override
    public List<TCheckgroup> selectTCheckgroupList(TCheckgroup tCheckgroup)
    {
        return tCheckgroupMapper.selectTCheckgroupList(tCheckgroup);
    }

    /**
     * 新增检查组
     *
     * @param dto 检查组
     * @return 结果
     */
    @Transactional
    @Override
    public int insertTCheckgroup(TCheckgroupDto dto)
    {
        dto.setCreateTime(DateUtils.getNowDate());
        int count=tCheckgroupMapper.insertTCheckgroup(dto);
        if (dto.getCheckItemIds()!=null&&dto.getCheckItemIds().size()>0){
            tCheckgroupCheckitemMapper .insertCheckitemIds(dto.getCheckItemIds(),dto.getId());
        }

        return count;
    }

//    /**
//     * 新增检查组管理
//     *
//     * @param tCheckgroup 检查组管理
//     * @return 结果
//     */
//    @Override
//    public int insertTCheckgroup(TCheckgroup tCheckgroup)
//    {
//        tCheckgroup.setCreateTime(DateUtils.getNowDate());
//        return tCheckgroupMapper.insertTCheckgroup(tCheckgroup);
//    }

    /**
     * 修改检查组
     *
     * @param dto 检查组
     * @return 结果
     */
    @Transactional
    @Override
    public int updateTCheckgroup(TCheckgroupDto dto)
    {
        try {
            //1.判断检查项id集合是否为空
            if (dto.getCheckItemIds() != null && dto.getCheckItemIds().size() > 0) {
                //2.删除原有的检查组和检查项关联关系
                tCheckgroupCheckitemMapper.deleteTCheckgroupCheckitemByCheckgroupId(dto.getId());
                //3.批量保存检查组和检查项的关联关系
                tCheckgroupCheckitemMapper.insertCheckitemIds(dto.getCheckItemIds(), dto.getId());
            }
            //4.更新检查组基本信息
            dto.setUpdateTime(DateUtils.getNowDate());
            return tCheckgroupMapper.updateTCheckgroup(dto);
        } catch (Exception e) {
            throw new RuntimeException("修改检查组信息失败");
        }
    }

    /*@Override
    public int updateTCheckgroup(TCheckgroup tCheckgroup)
    {
        tCheckgroup.setUpdateTime(DateUtils.getNowDate());
        return tCheckgroupMapper.updateTCheckgroup(tCheckgroup);
    }*/

    /**
     * 批量删除检查组管理
     * 
     * @param ids 需要删除的检查组管理主键
     * @return 结果
     */
    @Override
    public int deleteTCheckgroupByIds(Long[] ids)
    {
        return tCheckgroupMapper.deleteTCheckgroupByIds(ids);
    }

    /**
     * 删除检查组信息
     *
     * @param id 检查组主键
     * @return 结果
     */
    @Override
    public int deleteTCheckgroupById(Long id)
    {  //1。判读检查组是否被套餐关联
        int count = tSetmealCheckgroupMapper.selectCountByCheckgroupId(id);
        if (count > 0){
            throw new RuntimeException("当前检查组被套餐关联，不能删除");
        }
        //2.删除检查组和检查项的关联关系
        tCheckgroupCheckitemMapper.deleteTCheckgroupCheckitemByCheckgroupId(id);
        //3.删除检查组信息
        return tCheckgroupMapper.deleteTCheckgroupById(id);
    }
//    @Override
//    public int deleteTCheckgroupById(Long id)
//    {
//        return tCheckgroupMapper.deleteTCheckgroupById(id);
//    }
}
