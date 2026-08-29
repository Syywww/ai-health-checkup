package com.health.reservation.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.health.common.annotation.Excel;
import com.health.common.core.domain.BaseEntity;

/**
 * 预约设置对象 appointment_setting
 * 
 * @author syw
 * @date 2026-07-02
 */
public class AppointmentSetting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设置ID */
    private Long id;

    /** 设置日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "设置日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date settingDate;

    /** 最大预约数 */
    @Excel(name = "最大预约数")
    private Long maxCount;

    /** 已预约人数 */
    private Long reservedCount;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setSettingDate(Date settingDate) 
    {
        this.settingDate = settingDate;
    }

    public Date getSettingDate() 
    {
        return settingDate;
    }

    public void setMaxCount(Long maxCount) 
    {
        this.maxCount = maxCount;
    }

    public Long getMaxCount()
    {
        return maxCount;
    }

    public void setReservedCount(Long reservedCount)
    {
        this.reservedCount = reservedCount;
    }

    public Long getReservedCount()
    {
        return reservedCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("settingDate", getSettingDate())
            .append("maxCount", getMaxCount())
            .append("reservedCount", getReservedCount())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
