package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WgTableMapper;
import com.ruoyi.system.domain.WgTable;
import com.ruoyi.system.service.IWgTableService;

/**
 * 网格人员Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-23
 */
@Service
public class WgTableServiceImpl implements IWgTableService 
{
    @Autowired
    private WgTableMapper wgTableMapper;

    /**
     * 查询网格人员
     * 
     * @param wgId 网格人员主键
     * @return 网格人员
     */
    @Override
    public WgTable selectWgTableByWgId(Long wgId)
    {
        return wgTableMapper.selectWgTableByWgId(wgId);
    }

    /**
     * 查询网格人员列表
     * 
     * @param wgTable 网格人员
     * @return 网格人员
     */
    @Override
    public List<WgTable> selectWgTableList(WgTable wgTable)
    {
        return wgTableMapper.selectWgTableList(wgTable);
    }

    /**
     * 新增网格人员
     * 
     * @param wgTable 网格人员
     * @return 结果
     */
    @Override
    public int insertWgTable(WgTable wgTable)
    {
        return wgTableMapper.insertWgTable(wgTable);
    }

    /**
     * 修改网格人员
     * 
     * @param wgTable 网格人员
     * @return 结果
     */
    @Override
    public int updateWgTable(WgTable wgTable)
    {
        return wgTableMapper.updateWgTable(wgTable);
    }

    /**
     * 批量删除网格人员
     * 
     * @param wgIds 需要删除的网格人员主键
     * @return 结果
     */
    @Override
    public int deleteWgTableByWgIds(Long[] wgIds)
    {
        return wgTableMapper.deleteWgTableByWgIds(wgIds);
    }

    /**
     * 删除网格人员信息
     * 
     * @param wgId 网格人员主键
     * @return 结果
     */
    @Override
    public int deleteWgTableByWgId(Long wgId)
    {
        return wgTableMapper.deleteWgTableByWgId(wgId);
    }
}
