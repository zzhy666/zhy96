package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.AqiFeedbackMapper;
import com.ruoyi.system.domain.AqiFeedback;
import com.ruoyi.system.service.IAqiFeedbackService;

/**
 * 空气质量用户反馈Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-27
 */
@Service
public class AqiFeedbackServiceImpl implements IAqiFeedbackService 
{
    @Autowired
    private AqiFeedbackMapper aqiFeedbackMapper;

    /**
     * 查询空气质量用户反馈
     * 
     * @param feedbackId 空气质量用户反馈主键
     * @return 空气质量用户反馈
     */
    @Override
    public AqiFeedback selectAqiFeedbackByFeedbackId(Long feedbackId)
    {
        return aqiFeedbackMapper.selectAqiFeedbackByFeedbackId(feedbackId);
    }

    /**
     * 查询空气质量用户反馈列表
     * 
     * @param aqiFeedback 空气质量用户反馈
     * @return 空气质量用户反馈
     */
    @Override
    public List<AqiFeedback> selectAqiFeedbackList(AqiFeedback aqiFeedback)
    {
        return aqiFeedbackMapper.selectAqiFeedbackList(aqiFeedback);
    }

    /**
     * 新增空气质量用户反馈
     * 
     * @param aqiFeedback 空气质量用户反馈
     * @return 结果
     */
    @Override
    public int insertAqiFeedback(AqiFeedback aqiFeedback)
    {
        return aqiFeedbackMapper.insertAqiFeedback(aqiFeedback);
    }

    /**
     * 修改空气质量用户反馈
     * 
     * @param aqiFeedback 空气质量用户反馈
     * @return 结果
     */
    @Override
    public int updateAqiFeedback(AqiFeedback aqiFeedback)
    {
        return aqiFeedbackMapper.updateAqiFeedback(aqiFeedback);
    }

    /**
     * 批量删除空气质量用户反馈
     * 
     * @param feedbackIds 需要删除的空气质量用户反馈主键
     * @return 结果
     */
    @Override
    public int deleteAqiFeedbackByFeedbackIds(Long[] feedbackIds)
    {
        return aqiFeedbackMapper.deleteAqiFeedbackByFeedbackIds(feedbackIds);
    }

    /**
     * 删除空气质量用户反馈信息
     * 
     * @param feedbackId 空气质量用户反馈主键
     * @return 结果
     */
    @Override
    public int deleteAqiFeedbackByFeedbackId(Long feedbackId)
    {
        return aqiFeedbackMapper.deleteAqiFeedbackByFeedbackId(feedbackId);
    }
}
