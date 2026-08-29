package com.health.reservation.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import com.health.common.exception.ServiceException;
import com.health.common.utils.DateUtils;
import com.health.common.utils.poi.ExcelUtil;
import com.health.reservation.dto.OrderSettingDTO;
import com.health.reservation.vo.OrderSettingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.health.reservation.mapper.AppointmentSettingMapper;
import com.health.reservation.domain.AppointmentSetting;
import com.health.reservation.service.IAppointmentSettingService;

/**
 * 预约设置Service业务层处理
 *
 * @author syw
 * @date 2026-07-02
 */
@Service
public class AppointmentSettingServiceImpl implements IAppointmentSettingService
{
    @Autowired
    private AppointmentSettingMapper appointmentSettingMapper;

    /**
     * 查询预约设置
     *
     * @param id 预约设置主键
     * @return 预约设置
     */
    @Override
    public AppointmentSetting selectAppointmentSettingById(Long id)
    {
        return appointmentSettingMapper.selectAppointmentSettingById(id);
    }

    /**
     * 查询预约设置列表
     *
     * @param appointmentSetting 预约设置
     * @return 预约设置
     */
    @Override
    public List<AppointmentSetting> selectAppointmentSettingList(AppointmentSetting appointmentSetting)
    {
        return appointmentSettingMapper.selectAppointmentSettingList(appointmentSetting);
    }

    /**
     * 新增预约设置
     *
     * @param appointmentSetting 预约设置
     * @return 结果
     */
    @Override
    public int insertAppointmentSetting(AppointmentSetting appointmentSetting)
    {
        appointmentSetting.setCreateTime(DateUtils.getNowDate());
        return appointmentSettingMapper.insertAppointmentSetting(appointmentSetting);
    }

    /**
     * 修改预约设置
     *
     * @param appointmentSetting 预约设置
     * @return 结果
     */
    @Override
    public int updateAppointmentSetting(AppointmentSetting appointmentSetting)
    {
        appointmentSetting.setUpdateTime(DateUtils.getNowDate());
        return appointmentSettingMapper.updateAppointmentSetting(appointmentSetting);
    }

    /**
     * 批量删除预约设置
     *
     * @param ids 需要删除的预约设置主键
     * @return 结果
     */
    @Override
    public int deleteAppointmentSettingByIds(Long[] ids)
    {
        return appointmentSettingMapper.deleteAppointmentSettingByIds(ids);
    }

    /**
     * 删除预约设置信息
     *
     * @param id 预约设置主键
     * @return 结果
     */
    @Override
    public int deleteAppointmentSettingById(Long id)
    {
        return appointmentSettingMapper.deleteAppointmentSettingById(id);
    }

    /**
     * 根据年月查询当月预约设置（日历展示）
     */
    @Override
    public List<OrderSettingVO> getOrderSettingByMonth(String month)
    {
        if (month == null || !month.matches("\\d{4}-\\d{2}"))
        {
            throw new ServiceException("月份格式错误，应为 YYYY-MM");
        }
        List<AppointmentSetting> list = appointmentSettingMapper.selectByYearMonth(month);
        return list.stream().map(this::toVO).collect(Collectors.toList());
    }

    /**
     * 新增某天预约设置（按日期唯一，已存在则视为修改，避免重复放号）
     */
    @Override
    public OrderSettingVO insertOrderSetting(OrderSettingDTO dto)
    {
        if (dto == null || dto.getOrderDate() == null)
        {
            throw new ServiceException("放号日期不能为空");
        }
        if (dto.getNumber() == null || dto.getNumber() <= 0)
        {
            throw new ServiceException("可预约人数必须大于 0");
        }
        AppointmentSetting setting = new AppointmentSetting();
        setting.setSettingDate(DateUtils.parseDate(dto.getOrderDate()));
        setting.setMaxCount(dto.getNumber().longValue());

        AppointmentSetting existing = appointmentSettingMapper.selectBySettingDate(setting.getSettingDate());
        if (existing != null)
        {
            // 该日期已放号，视为修改
            setting.setId(existing.getId());
            appointmentSettingMapper.updateAppointmentSetting(setting);
        }
        else
        {
            setting.setCreateTime(DateUtils.getNowDate());
            appointmentSettingMapper.insertAppointmentSetting(setting);
        }
        return toVO(setting);
    }

    /**
     * 修改某天可预约人数
     */
    @Override
    public int updateNumberByOrderDate(OrderSettingDTO dto)
    {
        if (dto == null || dto.getId() == null)
        {
            throw new ServiceException("设置ID不能为空");
        }
        AppointmentSetting setting = new AppointmentSetting();
        setting.setId(dto.getId());
        setting.setSettingDate(DateUtils.parseDate(dto.getOrderDate()));
        setting.setMaxCount(dto.getNumber() == null ? 0L : dto.getNumber().longValue());
        setting.setUpdateTime(DateUtils.getNowDate());
        return appointmentSettingMapper.updateAppointmentSetting(setting);
    }

    /**
     * 导入预约设置 Excel（按日期唯一，存在则更新，不存在则新增）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importOrderSetting(MultipartFile file) throws Exception
    {
        ExcelUtil<AppointmentSetting> util = new ExcelUtil<AppointmentSetting>(AppointmentSetting.class);
        List<AppointmentSetting> list = util.importExcel(file.getInputStream());
        if (list == null || list.isEmpty())
        {
            throw new ServiceException("Excel 文件内容为空");
        }
        List<AppointmentSetting> insertList = new ArrayList<>();
        List<AppointmentSetting> updateList = new ArrayList<>();
        for (AppointmentSetting setting : list)
        {
            if (setting == null || setting.getSettingDate() == null || setting.getMaxCount() == null)
            {
                continue;
            }
            AppointmentSetting existing = appointmentSettingMapper.selectBySettingDate(setting.getSettingDate());
            if (existing != null)
            {
                setting.setId(existing.getId());
                updateList.add(setting);
            }
            else
            {
                insertList.add(setting);
            }
        }
        if (!insertList.isEmpty())
        {
            appointmentSettingMapper.insertBatch(insertList);
        }
        for (AppointmentSetting item : updateList)
        {
            appointmentSettingMapper.updateAppointmentSetting(item);
        }
    }

    /**
     * 预约设置实体 -> 日历 VO（date 取"日"，number/reservations 转 Integer）
     */
    private OrderSettingVO toVO(AppointmentSetting setting)
    {
        OrderSettingVO vo = new OrderSettingVO();
        vo.setId(setting.getId());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(setting.getSettingDate());
        vo.setDate(calendar.get(Calendar.DAY_OF_MONTH));
        vo.setNumber(setting.getMaxCount() == null ? 0 : setting.getMaxCount().intValue());
        vo.setReservations(setting.getReservedCount() == null ? 0 : setting.getReservedCount().intValue());
        return vo;
    }
}