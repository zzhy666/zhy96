package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 空气质量用户反馈对象 aqi_feedback
 * 
 * @author ruoyi
 * @date 2026-04-27
 */
public class AqiFeedback extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 反馈ID */
    private Long feedbackId;
    /** 检测员ID */
    @Excel(name = "检测员ID")
    private Long inspectorId;

    /** 检测员姓名 */
    @Excel(name = "检测员姓名")
    private String inspectorName;

    /** 省份ID */
    @Excel(name = "省份ID")
    private Long provinceId;

    /** 省份名称 */
    @Excel(name = "省份名称")
    private String provinceName;

    /** 城市ID */
    @Excel(name = "城市ID")
    private Long cityId;

    /** 城市名称 */
    @Excel(name = "城市名称")
    private String cityName;

    /** 详细地址 */
    @Excel(name = "详细地址")
    private String detailAddress;

    /** 预估AQI等级ID (1-6) */
    @Excel(name = "预估AQI等级ID (1-6)")
    private Integer aqiLevelId;

    /** 预估AQI等级名称 */
    @Excel(name = "预估AQI等级名称")
    private String aqiLevelName;

    /** 空气质量描述 */
    @Excel(name = "空气质量描述")
    private String airDescription;

    /** 反馈提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "反馈提交时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date feedbackTime;

    public void setFeedbackId(Long feedbackId) 
    {
        this.feedbackId = feedbackId;
    }

    public Long getFeedbackId() 
    {
        return feedbackId;
    }

    public void setProvinceId(Long provinceId) 
    {
        this.provinceId = provinceId;
    }

    public Long getProvinceId() 
    {
        return provinceId;
    }

    public void setProvinceName(String provinceName) 
    {
        this.provinceName = provinceName;
    }

    public String getProvinceName() 
    {
        return provinceName;
    }

    public void setCityId(Long cityId) 
    {
        this.cityId = cityId;
    }

    public Long getCityId() 
    {
        return cityId;
    }

    public void setCityName(String cityName) 
    {
        this.cityName = cityName;
    }

    public String getCityName() 
    {
        return cityName;
    }

    public void setDetailAddress(String detailAddress) 
    {
        this.detailAddress = detailAddress;
    }

    public String getDetailAddress() 
    {
        return detailAddress;
    }

    public void setAqiLevelId(Integer aqiLevelId) 
    {
        this.aqiLevelId = aqiLevelId;
    }

    public Integer getAqiLevelId() 
    {
        return aqiLevelId;
    }

    public void setAqiLevelName(String aqiLevelName) 
    {
        this.aqiLevelName = aqiLevelName;
    }

    public String getAqiLevelName() 
    {
        return aqiLevelName;
    }

    public void setAirDescription(String airDescription) 
    {
        this.airDescription = airDescription;
    }

    public String getAirDescription() 
    {
        return airDescription;
    }

    public void setFeedbackTime(Date feedbackTime) 
    {
        this.feedbackTime = feedbackTime;
    }

    public Date getFeedbackTime() 
    {
        return feedbackTime;
    }

    public Long getInspectorId() {
        return inspectorId;
    }

    public void setInspectorId(Long inspectorId) {
        this.inspectorId = inspectorId;
    }

    public String getInspectorName() {
        return inspectorName;
    }

    public void setInspectorName(String inspectorName) {
        this.inspectorName = inspectorName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("feedbackId", getFeedbackId())
            .append("provinceId", getProvinceId())
            .append("provinceName", getProvinceName())
            .append("cityId", getCityId())
            .append("cityName", getCityName())
            .append("detailAddress", getDetailAddress())
            .append("aqiLevelId", getAqiLevelId())
            .append("aqiLevelName", getAqiLevelName())
            .append("airDescription", getAirDescription())
            .append("feedbackTime", getFeedbackTime())
                .append("inspectorId", getInspectorId())
                .append("inspectorName", getInspectorName())
            .toString();
    }


}
