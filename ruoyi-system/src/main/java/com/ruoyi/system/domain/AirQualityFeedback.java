package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 空气质量反馈对象 air_quality_feedback
 */
public class AirQualityFeedback extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 反馈ID */
    private String id;

    /** 检测员ID */
    @Excel(name = "检测员ID")
    private Long inspectorId;

    /** 网格ID */
    @Excel(name = "网格ID")
    private Long gridId;

    /** 空气质量等级（1-6） */
    @Excel(name = "空气质量等级（1-6）")
    private Integer aqiLevel;

    /** 详细描述 */
    @Excel(name = "详细描述")
    private String description;

    /** 提交时间 */
    @Excel(name = "提交时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getInspectorId() { return inspectorId; }
    public void setInspectorId(Long inspectorId) { this.inspectorId = inspectorId; }

    public Long getGridId() { return gridId; }
    public void setGridId(Long gridId) { this.gridId = gridId; }

    public Integer getAqiLevel() { return aqiLevel; }
    public void setAqiLevel(Integer aqiLevel) { this.aqiLevel = aqiLevel; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("inspectorId", getInspectorId())
                .append("gridId", getGridId())
                .append("aqiLevel", getAqiLevel())
                .append("description", getDescription())
                .append("createTime", getCreateTime())
                .toString();
    }
}