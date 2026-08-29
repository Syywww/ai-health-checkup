package com.health.reservation.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.health.common.annotation.Excel;
import com.health.common.core.domain.BaseEntity;

/**
 * 套餐管理对象 t_setmeal
 * 
 * @author ruoyi
 * @date 2026-06-30
 */
public class TSetmeal extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 套餐ID */
    private Long id;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String name;

    /** 项目编码 */
    @Excel(name = "项目编码")
    private String code;

    /** 助记码 */
    @Excel(name = "助记码")
    private String helpCode;

    /** 适用性别 */
    @Excel(name = "适用性别")
    private String sex;

    /** 适用年龄 */
    @Excel(name = "适用年龄")
    private String age;

    /** 价格 */
    @Excel(name = "价格")
    private Long price;

    /** 注意事项 */
    @Excel(name = "注意事项")
    private String attention;

    /** 图片路径 */
    @Excel(name = "图片路径")
    private String img;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }

    public void setHelpCode(String helpCode) 
    {
        this.helpCode = helpCode;
    }

    public String getHelpCode() 
    {
        return helpCode;
    }

    public void setSex(String sex) 
    {
        this.sex = sex;
    }

    public String getSex() 
    {
        return sex;
    }

    public void setAge(String age) 
    {
        this.age = age;
    }

    public String getAge() 
    {
        return age;
    }

    public void setPrice(Long price) 
    {
        this.price = price;
    }

    public Long getPrice() 
    {
        return price;
    }

    public void setAttention(String attention) 
    {
        this.attention = attention;
    }

    public String getAttention() 
    {
        return attention;
    }

    public void setImg(String img) 
    {
        this.img = img;
    }

    public String getImg() 
    {
        return img;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("code", getCode())
            .append("helpCode", getHelpCode())
            .append("sex", getSex())
            .append("age", getAge())
            .append("price", getPrice())
            .append("remark", getRemark())
            .append("attention", getAttention())
            .append("img", getImg())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
