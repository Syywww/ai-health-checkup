package com.health.reservation.mapper;

import java.util.List;
import com.health.reservation.domain.TSetmeal;

/**
 * 套餐管理Mapper接口
 * 
 * @author ruoyi
 * @date 2026-06-30
 */
public interface TSetmealMapper 
{
    /**
     * 查询套餐管理
     * 
     * @param id 套餐管理主键
     * @return 套餐管理
     */
    public TSetmeal selectTSetmealById(Long id);

    /**
     * 查询套餐管理列表
     * 
     * @param tSetmeal 套餐管理
     * @return 套餐管理集合
     */
    public List<TSetmeal> selectTSetmealList(TSetmeal tSetmeal);

    /**
     * 新增套餐管理
     * 
     * @param tSetmeal 套餐管理
     * @return 结果
     */
    public int insertTSetmeal(TSetmeal tSetmeal);

    /**
     * 修改套餐管理
     * 
     * @param tSetmeal 套餐管理
     * @return 结果
     */
    public int updateTSetmeal(TSetmeal tSetmeal);

    /**
     * 删除套餐管理
     * 
     * @param id 套餐管理主键
     * @return 结果
     */
    public int deleteTSetmealById(Long id);

    /**
     * 批量删除套餐管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTSetmealByIds(Long[] ids);
}
