package com.ruoyi.system.domain.vo;

import com.ruoyi.system.domain.AqiBz;
import com.ruoyi.system.domain.AqiFeedback;
import com.ruoyi.system.domain.Inspector;

import java.util.Date;

public class CheckRecordVO {
    private Long recordId;
    private Long inspectorId;
    private Integer aqiLevelId;
    private Date checkDate;
    private String checkTime;
    private String location;
    private Date submitTime;
   private AqiBz aqiBz;
   private Inspector inspector;
    private Long feedbackId;
    private AqiFeedback aqiFeedback;
    private String additionalData;
    private String aqiLevel;
    // 添加便捷获取检测员姓名的方法
    public String getInspectorName() {
        if (this.inspector != null) {
            return this.inspector.getName();
        }
        return null;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getInspectorId() {
        return inspectorId;
    }

    public void setInspectorId(Long inspectorId) {
        this.inspectorId = inspectorId;
    }

    public Integer getAqiLevelId() {
        return aqiLevelId;
    }

    public void setAqiLevelId(Integer aqiLevelId) {
        this.aqiLevelId = aqiLevelId;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public AqiBz getAqiBz() {
        return aqiBz;
    }

    public void setAqiBz(AqiBz aqiBz) {
        this.aqiBz = aqiBz;
    }

    public Inspector getInspector() {
        return inspector;
    }

    public void setInspector(Inspector inspector) {
        this.inspector = inspector;
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public AqiFeedback getAqiFeedback() {
        return aqiFeedback;
    }

    public void setAqiFeedback(AqiFeedback aqiFeedback) {
        this.aqiFeedback = aqiFeedback;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

    public String getAqiLevel() {
        if (this.aqiBz != null) {
            return this.aqiBz.getAqiLevel();
        }
        // 否则返回自身的aqiLevel字段（可能为null或备用值）
        return this.aqiLevel;
    }

    public void setAqiLevel(String aqiLevel) {
        this.aqiLevel = aqiLevel;
    }

    @Override
    public String toString() {
        return "CheckRecordVO{" +
                "recordId=" + recordId +
                ", inspectorId=" + inspectorId +
                ", aqiLevelId=" + aqiLevelId +
                ", checkDate=" + checkDate +
                ", checkTime='" + checkTime + '\'' +
                ", location='" + location + '\'' +
                ", submitTime=" + submitTime +
                ", aqiBz=" + aqiBz +
                ", inspector=" + inspector +
                ", aqiFeedback=" + aqiFeedback +
                ", additionalData='" + additionalData  +
                ", aqiLevel='" + aqiLevel  +
                '}';
    }
}