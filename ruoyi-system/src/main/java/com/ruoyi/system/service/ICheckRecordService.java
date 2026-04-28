package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.CheckRecord;
import com.ruoyi.system.domain.vo.CheckRecordVO;

/**
 * 检查记录提交Service接口
 * 
 * @author ruoyi
 * @date 2026-04-22
 */
public interface ICheckRecordService 
{
    /**
     * 查询检查记录提交
     * 
     * @param recordId 检查记录提交主键
     * @return 检查记录提交
     */
    public CheckRecord selectCheckRecordByRecordId(Long recordId);

    /**
     * 查询检查记录提交列表
     * 
     * @param checkRecord 检查记录提交
     * @return 检查记录提交集合
     */
    public List<CheckRecord> selectCheckRecordList(CheckRecord checkRecord);

    /**
     * 新增检查记录提交
     * 
     * @param checkRecord 检查记录提交
     * @return 结果
     */
    public int insertCheckRecord(CheckRecord checkRecord);

    /**
     * 修改检查记录提交
     * 
     * @param checkRecord 检查记录提交
     * @return 结果
     */
    public int updateCheckRecord(CheckRecord checkRecord);

    /**
     * 批量删除检查记录提交
     * 
     * @param recordIds 需要删除的检查记录提交主键集合
     * @return 结果
     */
    public int deleteCheckRecordByRecordIds(Long[] recordIds);

    /**
     * 删除检查记录提交信息
     * 
     * @param recordId 检查记录提交主键
     * @return 结果
     */
    public int deleteCheckRecordByRecordId(Long recordId);

    CheckRecordVO selectCheckRecordVOByRecordId(Long recordId);
    List<CheckRecordVO> selectCheckRecordVOList(CheckRecordVO checkRecordVO);
    int dispatchCheckRecord(Long recordId, Long inspectorId);
}
