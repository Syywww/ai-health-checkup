package com.health.reservation.mapper;

import java.util.List;
import com.health.reservation.domain.TCheckgroupCheckitem;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 检查组检查项关联Mapper接口
 * 
 * @author ruoyi
 * @date 2026-07-01
 */
@Mapper
public interface TCheckgroupCheckitemMapper 
{
    /**
     * 查询检查组检查项关联
     * 
     * @param checkgroupId 检查组检查项关联主键
     * @return 检查组检查项关联
     */
    public TCheckgroupCheckitem selectTCheckgroupCheckitemByCheckgroupId(Long checkgroupId);

    /**
     * 查询检查组检查项关联列表
     * 
     * @param tCheckgroupCheckitem 检查组检查项关联
     * @return 检查组检查项关联集合
     */
    public List<TCheckgroupCheckitem> selectTCheckgroupCheckitemList(TCheckgroupCheckitem tCheckgroupCheckitem);

    /**
     * 新增检查组检查项关联
     * 
     * @param tCheckgroupCheckitem 检查组检查项关联
     * @return 结果
     */
    public int insertTCheckgroupCheckitem(TCheckgroupCheckitem tCheckgroupCheckitem);

    /**
     * 修改检查组检查项关联
     * 
     * @param tCheckgroupCheckitem 检查组检查项关联
     * @return 结果
     */
    public int updateTCheckgroupCheckitem(TCheckgroupCheckitem tCheckgroupCheckitem);

    /**
     * 删除检查组检查项关联
     * 
     * @param checkgroupId 检查组检查项关联主键
     * @return 结果
     */
    public int deleteTCheckgroupCheckitemByCheckgroupId(Long checkgroupId);

    /**
     * 批量删除检查组检查项关联
     * 
     * @param checkgroupIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTCheckgroupCheckitemByCheckgroupIds(Long[] checkgroupIds);

    int insertCheckitemIds(@Param("list") List<Long> checkItemIds, @Param("groupId") Long checkgroupId);

    @Select("select checkitem_id from t_checkgroup_checkitem where checkgroup_id=#{id}")
    List<Long> selectCheckitemIds(Long id);
}
