package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 网格人员对象 wg_table
 * 
 * @author ruoyi
 * @date 2026-04-23
 */
public class WgTable extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long wgId;

    /** 姓名 */
    @Excel(name = "姓名")
    private String wgName;

    public void setWgId(Long wgId) 
    {
        this.wgId = wgId;
    }

    public Long getWgId() 
    {
        return wgId;
    }

    public void setWgName(String wgName) 
    {
        this.wgName = wgName;
    }

    public String getWgName() 
    {
        return wgName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("wgId", getWgId())
            .append("wgName", getWgName())
            .toString();
    }
}
