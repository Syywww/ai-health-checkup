package com.health.reservation.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.health.reservation.domain.AppointmentSetting;
import com.health.reservation.dto.OrderSettingDTO;
import com.health.reservation.vo.OrderSettingVO;

/**
 * 预约设置Service接口
 *
 * @author syw
 * @date 2026-07-02
 */
public interface IAppointmentSettingService
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
     * 批量删除预约设置
     *
     * @param ids 需要删除的预约设置主键集合
     * @return 结果
     */
    public int deleteAppointmentSettingByIds(Long[] ids);

    /**
     * 删除预约设置信息
     *
     * @param id 预约设置主键
     * @return 结果
     */
    public int deleteAppointmentSettingById(Long id);

    /**
     * 根据年月查询当月预约设置（日历展示）
     *
     * @param month 年月，格式 YYYY-MM
     * @return 预约设置 VO 集合
     */
    public List<OrderSettingVO> getOrderSettingByMonth(String month);

    /**
     * 新增某天预约设置（按日期唯一，已存在则视为修改）
     *
     * @param dto 放号日期 + 可预约人数
     * @return 新增/修改后的预约设置 VO
     */
    public OrderSettingVO insertOrderSetting(OrderSettingDTO dto);

    /**
     * 修改某天可预约人数
     *
     * @param dto 设置ID + 放号日期 + 可预约人数
     * @return 结果
     */
    public int updateNumberByOrderDate(OrderSettingDTO dto);

    /**
     * 导入预约设置 Excel（按日期唯一，存在则更新，不存在则新增）
     *
     * @param file Excel 文件
     */
    public void importOrderSetting(MultipartFile file) throws Exception;
}
