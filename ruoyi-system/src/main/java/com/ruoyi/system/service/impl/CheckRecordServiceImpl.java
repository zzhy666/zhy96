package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.AqiBz;
import com.ruoyi.system.domain.Inspector;
import com.ruoyi.system.domain.vo.CheckRecordVO;
import com.ruoyi.system.mapper.InspectorMapper;
import com.ruoyi.system.service.IAqiBzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.CheckRecordMapper;
import com.ruoyi.system.domain.CheckRecord;
import com.ruoyi.system.service.ICheckRecordService;

import org.springframework.transaction.annotation.Transactional;


/**
 * 检查记录提交Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-22
 */
@Service
public class CheckRecordServiceImpl implements ICheckRecordService 
{
    @Autowired
    private CheckRecordMapper checkRecordMapper;
    @Autowired
    private IAqiBzService aqiBzService;
    @Autowired
    private InspectorMapper inspectorMapper;

    /**
     * 查询检查记录提交
     * 
     * @param recordId 检查记录提交主键
     * @return 检查记录提交
     */
    @Override
    public CheckRecord selectCheckRecordByRecordId(Long recordId)
    {
        return checkRecordMapper.selectCheckRecordByRecordId(recordId);
    }

    /**
     * 查询检查记录提交列表
     * 
     * @param checkRecord 检查记录提交
     * @return 检查记录提交
     */
    @Override
    public List<CheckRecord> selectCheckRecordList(CheckRecord checkRecord)
    {
        List<CheckRecord> list = checkRecordMapper.selectCheckRecordList(checkRecord);
        // 为每条记录设置 AQI 级别名称
        for (CheckRecord record : list) {
            if (record.getAqiLevelId() != null) {
                // 查询 AQI 级别信息
                AqiBz aqiBz = aqiBzService.selectAqiBzByLevelId(record.getAqiLevelId());
                if (aqiBz != null) {
                    // 由于 CheckRecord 实体没有 aqiLevel 字段，我们可以创建一个 Map 来存储
                    // 或者通过其他方式传递。这里简单记录到日志
                    System.out.println("记录 ID: " + record.getRecordId() +
                            ", AQI 级别 ID: " + record.getAqiLevelId() +
                            ", AQI 级别名称: " + aqiBz.getAqiLevel());
                }
            }
        }

        return list;
    }

    /**
     * 新增检查记录提交
     * 
     * @param checkRecord 检查记录提交
     * @return 结果
     */
    @Override
    public int insertCheckRecord(CheckRecord checkRecord)
    {
        return checkRecordMapper.insertCheckRecord(checkRecord);
    }

    /**
     * 修改检查记录提交
     * 
     * @param checkRecord 检查记录提交
     * @return 结果
     */
    @Override
    public int updateCheckRecord(CheckRecord checkRecord)
    {
        return checkRecordMapper.updateCheckRecord(checkRecord);
    }

    /**
     * 批量删除检查记录提交
     * 
     * @param recordIds 需要删除的检查记录提交主键
     * @return 结果
     */
    @Override
    public int deleteCheckRecordByRecordIds(Long[] recordIds)
    {
        return checkRecordMapper.deleteCheckRecordByRecordIds(recordIds);
    }

    /**
     * 删除检查记录提交信息
     * 
     * @param recordId 检查记录提交主键
     * @return 结果
     */
    @Override
    public int deleteCheckRecordByRecordId(Long recordId)
    {
        return checkRecordMapper.deleteCheckRecordByRecordId(recordId);
    }
    @Override
    public List<CheckRecordVO> selectCheckRecordVOList(CheckRecordVO checkRecordVO) {
        return checkRecordMapper.selectCheckRecordVOList(checkRecordVO);
    }
    @Override
    public CheckRecordVO selectCheckRecordVOByRecordId(Long recordId) {
        return checkRecordMapper.selectCheckRecordVOByRecordId(recordId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int dispatchCheckRecord(Long recordId, Long inspectorId) {
        // 1. 验证记录是否存在
        CheckRecord checkRecord = checkRecordMapper.selectCheckRecordByRecordId(recordId);
        if (checkRecord == null) {
            throw new ServiceException("检测记录不存在");
        }

        // 2. 验证网格员是否存在
        Inspector inspector = inspectorMapper.selectInspectorById(inspectorId);
        if (inspector == null) {
            throw new ServiceException("网格员不存在");
        }

        // 3. 创建要更新的记录对象
        CheckRecord updateRecord = new CheckRecord();
        updateRecord.setRecordId(recordId);
        updateRecord.setInspectorId(inspectorId);
        updateRecord.setUpdateTime(DateUtils.getNowDate()); // 设置更新时间

        // 4. 执行更新
        return checkRecordMapper.updateCheckRecord(updateRecord);
    }
}
