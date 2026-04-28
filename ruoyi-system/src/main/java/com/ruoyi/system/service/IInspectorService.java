package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Inspector;

/**
 * 人员Service接口
 * 
 * @author ruoyi
 * @date 2026-04-22
 */
public interface IInspectorService 
{
    /**
     * 查询人员
     * 
     * @param id 人员主键
     * @return 人员
     */
    public Inspector selectInspectorById(Long id);

    /**
     * 查询人员列表
     * 
     * @param inspector 人员
     * @return 人员集合
     */
    public List<Inspector> selectInspectorList(Inspector inspector);

    /**
     * 新增人员
     * 
     * @param inspector 人员
     * @return 结果
     */
    public int insertInspector(Inspector inspector);

    /**
     * 修改人员
     * 
     * @param inspector 人员
     * @return 结果
     */
    public int updateInspector(Inspector inspector);

    /**
     * 批量删除人员
     * 
     * @param ids 需要删除的人员主键集合
     * @return 结果
     */
    public int deleteInspectorByIds(Long[] ids);

    /**
     * 删除人员信息
     * 
     * @param id 人员主键
     * @return 结果
     */
    public int deleteInspectorById(Long id);
    int registerInspector(Inspector inspector);
    Inspector login(String phone, String password);
}
