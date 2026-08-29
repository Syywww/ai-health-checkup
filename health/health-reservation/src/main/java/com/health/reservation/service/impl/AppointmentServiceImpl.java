package com.health.reservation.service.impl;

import java.util.List;
import com.health.common.exception.ServiceException;
import com.health.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.health.reservation.mapper.AppointmentMapper;
import com.health.reservation.mapper.AppointmentSettingMapper;
import com.health.reservation.domain.Appointment;
import com.health.reservation.domain.AppointmentSetting;
import com.health.reservation.service.IAppointmentService;

/**
 * 预约记录Service业务层处理
 *
 * @author syw
 * @date 2026-07-02
 */
@Service
public class AppointmentServiceImpl implements IAppointmentService
{
    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private AppointmentSettingMapper appointmentSettingMapper;

    /**
     * 查询预约记录
     *
     * @param id 预约记录主键
     * @return 预约记录
     */
    @Override
    public Appointment selectAppointmentById(Long id)
    {
        return appointmentMapper.selectAppointmentById(id);
    }

    /**
     * 查询预约记录列表
     *
     * @param appointment 预约记录
     * @return 预约记录
     */
    @Override
    public List<Appointment> selectAppointmentList(Appointment appointment)
    {
        return appointmentMapper.selectAppointmentList(appointment);
    }

    /**
     * 新增预约记录（联动预约设置：校验放号、已满判断、原子扣减人数）
     * <p>
     * 预约设置中未放号的日期不允许预约；已预约人数达到可预约人数则提示已满。
     * 扣减采用 update ... where reserved_count &lt; max_count 原子判断，
     * 并发下不会出现超卖。
     *
     * @param appointment 预约记录
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertAppointment(Appointment appointment)
    {
        if (appointment.getAppointmentDate() == null)
        {
            throw new ServiceException("预约日期不能为空");
        }
        // 1. 校验该日期是否已放号
        AppointmentSetting setting = appointmentSettingMapper.selectBySettingDate(appointment.getAppointmentDate());
        if (setting == null)
        {
            throw new ServiceException("该日期未开放预约，请先在预约设置中放号");
        }
        // 2. 原子扣减：已预约人数 +1，未满才成功（避免并发超卖）
        if (appointmentSettingMapper.increaseReservedCount(setting.getId()) == 0)
        {
            throw new ServiceException("该日期预约已满，请选择其他日期");
        }
        // 3. 保存预约记录
        appointment.setCreateTime(DateUtils.getNowDate());
        return appointmentMapper.insertAppointment(appointment);
    }

    /**
     * 修改预约记录
     *
     * @param appointment 预约记录
     * @return 结果
     */
    @Override
    public int updateAppointment(Appointment appointment)
    {
        appointment.setUpdateTime(DateUtils.getNowDate());
        return appointmentMapper.updateAppointment(appointment);
    }

    /**
     * 批量删除预约记录
     *
     * @param ids 需要删除的预约记录主键
     * @return 结果
     */
    @Override
    public int deleteAppointmentByIds(Long[] ids)
    {
        return appointmentMapper.deleteAppointmentByIds(ids);
    }

    /**
     * 删除预约记录信息
     *
     * @param id 预约记录主键
     * @return 结果
     */
    @Override
    public int deleteAppointmentById(Long id)
    {
        return appointmentMapper.deleteAppointmentById(id);
    }
}