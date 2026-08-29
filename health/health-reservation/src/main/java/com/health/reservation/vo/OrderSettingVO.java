package com.health.reservation.vo;

/**
 * 预约设置日历 VO
 * <p>
 * date 返回当月几号（1~31），供前端日历按天比对；number 为可预约人数，
 * reservations 为已预约人数。
 *
 * @author syw
 * @date 2026-08-29
 */
public class OrderSettingVO
{
    private Long id;

    /** 当月几号（1~31） */
    private Integer date;

    /** 可预约人数 */
    private Integer number;

    /** 已预约人数 */
    private Integer reservations;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getDate()
    {
        return date;
    }

    public void setDate(Integer date)
    {
        this.date = date;
    }

    public Integer getNumber()
    {
        return number;
    }

    public void setNumber(Integer number)
    {
        this.number = number;
    }

    public Integer getReservations()
    {
        return reservations;
    }

    public void setReservations(Integer reservations)
    {
        this.reservations = reservations;
    }
}