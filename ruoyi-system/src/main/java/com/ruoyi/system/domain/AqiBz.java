package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 空气质量指数(AQI)级别标准对象 aqi_bz
 * 
 * @author ruoyi
 * @date 2026-04-22
 */
public class AqiBz extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 级别编号（1-6） */
    private Integer levelId;

    /** 空气质量指数级别 */
    @Excel(name = "空气质量指数级别")
    private String aqiLevel;

    /** 空气质量描述 */
    @Excel(name = "空气质量描述")
    private String description;

    /** 颜色名称 */
    @Excel(name = "颜色名称")
    private String colorName;

    /** 颜色代码（十六进制） */
    @Excel(name = "颜色代码", readConverterExp = "十=六进制")
    private String colorCode;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedAt;

    public void setLevelId(Integer levelId) 
    {
        this.levelId = levelId;
    }

    public Integer getLevelId() 
    {
        return levelId;
    }

    public void setAqiLevel(String aqiLevel) 
    {
        this.aqiLevel = aqiLevel;
    }

    public String getAqiLevel() 
    {
        return aqiLevel;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setColorName(String colorName) 
    {
        this.colorName = colorName;
    }

    public String getColorName() 
    {
        return colorName;
    }

    public void setColorCode(String colorCode) 
    {
        this.colorCode = colorCode;
    }

    public String getColorCode() 
    {
        return colorCode;
    }

    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    public void setUpdatedAt(Date updatedAt) 
    {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt() 
    {
        return updatedAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("levelId", getLevelId())
            .append("aqiLevel", getAqiLevel())
            .append("description", getDescription())
            .append("colorName", getColorName())
            .append("colorCode", getColorCode())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .toString();
    }
}
