package com.health.reservation.dto;

/**
 * 预约设置日历操作 DTO
 * <p>
 * 新增/修改某天可预约人数时使用：orderDate 为放号日期（YYYY-MM-DD），number 为可预约人数。
 * 新增时 id 为空，修改时前端回传 id。
 *
 * @author syw
 * @date 2026-08-29
 */
public class OrderSettingDTO
{
    /** 设置ID（修改时必传） */
    private Long id;

    /** 放号日期，格式 YYYY-MM-DD */
    private String orderDate;

    /** 可预约人数 */
    private Integer number;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getOrderDate()
    {
        return orderDate;
    }

    public void setOrderDate(String orderDate)
    {
        this.orderDate = orderDate;
    }

    public Integer getNumber()
    {
        return number;
    }

    public void setNumber(Integer number)
    {
        this.number = number;
    }
}