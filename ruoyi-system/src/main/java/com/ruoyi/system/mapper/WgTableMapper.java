package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.WgTable;

/**
 * 网格人员Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-23
 */
public interface WgTableMapper 
{
    /**
     * 查询网格人员
     * 
     * @param wgId 网格人员主键
     * @return 网格人员
     */
    public WgTable selectWgTableByWgId(Long wgId);

    /**
     * 查询网格人员列表
     * 
     * @param wgTable 网格人员
     * @return 网格人员集合
     */
    public List<WgTable> selectWgTableList(WgTable wgTable);

    /**
     * 新增网格人员
     * 
     * @param wgTable 网格人员
     * @return 结果
     */
    public int insertWgTable(WgTable wgTable);

    /**
     * 修改网格人员
     * 
     * @param wgTable 网格人员
     * @return 结果
     */
    public int updateWgTable(WgTable wgTable);

    /**
     * 删除网格人员
     * 
     * @param wgId 网格人员主键
     * @return 结果
     */
    public int deleteWgTableByWgId(Long wgId);

    /**
     * 批量删除网格人员
     * 
     * @param wgIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWgTableByWgIds(Long[] wgIds);
}
