package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.AqiFeedback;

/**
 * 空气质量用户反馈Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-27
 */
public interface AqiFeedbackMapper 
{
    /**
     * 查询空气质量用户反馈
     * 
     * @param feedbackId 空气质量用户反馈主键
     * @return 空气质量用户反馈
     */
    public AqiFeedback selectAqiFeedbackByFeedbackId(Long feedbackId);

    /**
     * 查询空气质量用户反馈列表
     * 
     * @param aqiFeedback 空气质量用户反馈
     * @return 空气质量用户反馈集合
     */
    public List<AqiFeedback> selectAqiFeedbackList(AqiFeedback aqiFeedback);

    /**
     * 新增空气质量用户反馈
     * 
     * @param aqiFeedback 空气质量用户反馈
     * @return 结果
     */
    public int insertAqiFeedback(AqiFeedback aqiFeedback);

    /**
     * 修改空气质量用户反馈
     * 
     * @param aqiFeedback 空气质量用户反馈
     * @return 结果
     */
    public int updateAqiFeedback(AqiFeedback aqiFeedback);

    /**
     * 删除空气质量用户反馈
     * 
     * @param feedbackId 空气质量用户反馈主键
     * @return 结果
     */
    public int deleteAqiFeedbackByFeedbackId(Long feedbackId);

    /**
     * 批量删除空气质量用户反馈
     * 
     * @param feedbackIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAqiFeedbackByFeedbackIds(Long[] feedbackIds);
}
