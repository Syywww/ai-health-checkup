package com.health.reservation.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.health.common.annotation.Excel;
import com.health.common.core.domain.BaseEntity;

/**
 * 预约记录对象 appointment
 * 
 * @author syw
 * @date 2026-07-02
 */
public class Appointment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 预约ID */
    private Long id;

    /** 会员ID */
    @Excel(name = "会员ID")
    private Long memberId;

    /** 套餐ID */
    @Excel(name = "套餐ID")
    private Long setmealId;

    /** 预约日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预约日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date appointmentDate;

    /** 预约时段 */
    @Excel(name = "预约时段")
    private String appointmentTime;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 预约来源 */
    @Excel(name = "预约来源")
    private String source;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setMemberId(Long memberId) 
    {
        this.memberId = memberId;
    }

    public Long getMemberId() 
    {
        return memberId;
    }

    public void setSetmealId(Long setmealId) 
    {
        this.setmealId = setmealId;
    }

    public Long getSetmealId() 
    {
        return setmealId;
    }

    public void setAppointmentDate(Date appointmentDate) 
    {
        this.appointmentDate = appointmentDate;
    }

    public Date getAppointmentDate() 
    {
        return appointmentDate;
    }

    public void setAppointmentTime(String appointmentTime) 
    {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentTime() 
    {
        return appointmentTime;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setSource(String source) 
    {
        this.source = source;
    }

    public String getSource() 
    {
        return source;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("memberId", getMemberId())
            .append("setmealId", getSetmealId())
            .append("appointmentDate", getAppointmentDate())
            .append("appointmentTime", getAppointmentTime())
            .append("status", getStatus())
            .append("source", getSource())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
