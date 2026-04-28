package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 检查记录提交对象 check_record
 * 
 * @author ruoyi
 * @date 2026-04-22
 */
public class CheckRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录编号 */
    private Long recordId;

    /** 检测员ID，外键关联inspector表(BIGINT) */
    @Excel(name = "检测员ID，外键关联inspector表(BIGINT)")
    private Long inspectorId;

    /** AQI级别ID，外键关联aqi_bz表 */
    @Excel(name = "AQI级别ID，外键关联aqi_bz表")
    private Integer aqiLevelId;

    /** 检测日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "检测日期", width = 30, dateFormat = "yyyy-MM-dd")
    private String checkDate;

    /** 检测时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "检测时间", width = 30, dateFormat = "yyyy-MM-dd")
    private String checkTime;

    /** 检测地点 */
    @Excel(name = "检测地点")
    private String location;

    /** 附加数据 */
    @Excel(name = "附加数据")
    private String additionalData;

    /** 提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "提交时间", width = 30, dateFormat = "yyyy-MM-dd")
    private String submitTime;
    private String inspectorName;
    private Long feedbackId;
    private String dispatchStatus;
    private String detectStatus;


    public void setRecordId(Long recordId) 
    {
        this.recordId = recordId;
    }

    public Long getRecordId() 
    {
        return recordId;
    }

    public void setInspectorId(Long inspectorId) 
    {
        this.inspectorId = inspectorId;
    }

    public Long getInspectorId() 
    {
        return inspectorId;
    }

    public void setAqiLevelId(Integer aqiLevelId) 
    {
        this.aqiLevelId = aqiLevelId;
    }

    public Integer getAqiLevelId() 
    {
        return aqiLevelId;
    }

    public void setCheckDate(String checkDate)
    {
        this.checkDate = checkDate;
    }

    public String getCheckDate()
    {
        return checkDate;
    }

    public void setCheckTime(String checkTime)
    {
        this.checkTime = checkTime;
    }

    public String getCheckTime()
    {
        return checkTime;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public void setAdditionalData(String additionalData) 
    {
        this.additionalData = additionalData;
    }

    public String getAdditionalData() 
    {
        return additionalData;
    }

    public void setSubmitTime(String submitTime)
    {
        this.submitTime = submitTime;
    }

    public String getSubmitTime()
    {
        return submitTime;
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getDispatchStatus() {
        return dispatchStatus;
    }

    public void setDispatchStatus(String dispatchStatus) {
        this.dispatchStatus = dispatchStatus;
    }

    public String getDetectStatus() {
        return detectStatus;
    }

    public void setDetectStatus(String detectStatus) {
        this.detectStatus = detectStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("recordId", getRecordId())
            .append("inspectorId", getInspectorId())
            .append("aqiLevelId", getAqiLevelId())
            .append("checkDate", getCheckDate())
            .append("checkTime", getCheckTime())
            .append("location", getLocation())
            .append("additionalData", getAdditionalData())
            .append("submitTime", getSubmitTime())
                .append("inspectorName", getInspectorName())
                .append("feedbackId", getFeedbackId())
                .append("dispatchStatus", getDispatchStatus())
                .append("detectStatus", getDetectStatus())
            .toString();
    }

    public String getInspectorName() {
        return inspectorName;
    }

    public void setInspectorName(String inspectorName) {
        this.inspectorName = inspectorName;
    }

}
