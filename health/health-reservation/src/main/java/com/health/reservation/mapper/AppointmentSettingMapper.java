package com.health.reservation.mapper;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.health.reservation.domain.AppointmentSetting;

/**
 * 预约设置Mapper接口
 *
 * @author syw
 * @date 2026-07-02
 */
public interface AppointmentSettingMapper
{
    /**
     * 查询预约设置
     * 
     * @param id 预约设置主键
     * @return 预约设置
     */
    public AppointmentSetting selectAppointmentSettingById(Long id);

    /**
     * 查询预约设置列表
     * 
     * @param appointmentSetting 预约设置
     * @return 预约设置集合
     */
    public List<AppointmentSetting> selectAppointmentSettingList(AppointmentSetting appointmentSetting);

    /**
     * 新增预约设置
     * 
     * @param appointmentSetting 预约设置
     * @return 结果
     */
    public int insertAppointmentSetting(AppointmentSetting appointmentSetting);

    /**
     * 修改预约设置
     * 
     * @param appointmentSetting 预约设置
     * @return 结果
     */
    public int updateAppointmentSetting(AppointmentSetting appointmentSetting);

    /**
     * 删除预约设置
     * 
     * @param id 预约设置主键
     * @return 结果
     */
    public int deleteAppointmentSettingById(Long id);

    /**
     * 批量删除预约设置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAppointmentSettingByIds(Long[] ids);

    /**
     * 根据年月查询预约设置（例如 2026-09），用于日历按月展示
     *
     * @param yearMonth 年月，格式 YYYY-MM
     * @return 预约设置集合
     */
    public List<AppointmentSetting> selectByYearMonth(@Param("yearMonth") String yearMonth);

    /**
     * 根据放号日期查询预约设置
     *
     * @param settingDate 放号日期
     * @return 预约设置
     */
    public AppointmentSetting selectBySettingDate(@Param("settingDate") Date settingDate);

    /**
     * 批量新增预约设置（Excel 导入使用）
     *
     * @param list 预约设置集合
     * @return 结果
     */
    public int insertBatch(@Param("list") List<AppointmentSetting> list);

    /**
     * 已预约人数原子 +1（未满才成功）
     * <p>
     * 通过 where reserved_count &lt; max_count 保证并发下不会超卖：
     * 返回 1 表示扣减成功，返回 0 表示当日已满。
     *
     * @param id 预约设置主键
     * @return 结果
     */
    public int increaseReservedCount(@Param("id") Long id);
}
