package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.AqiBz;

/**
 * 空气质量指数(AQI)级别标准Service接口
 * 
 * @author ruoyi
 * @date 2026-04-22
 */
public interface IAqiBzService 
{
    /**
     * 查询空气质量指数(AQI)级别标准
     * 
     * @param levelId 空气质量指数(AQI)级别标准主键
     * @return 空气质量指数(AQI)级别标准
     */
    public AqiBz selectAqiBzByLevelId(Integer levelId);

    /**
     * 查询空气质量指数(AQI)级别标准列表
     * 
     * @param aqiBz 空气质量指数(AQI)级别标准
     * @return 空气质量指数(AQI)级别标准集合
     */
    public List<AqiBz> selectAqiBzList(AqiBz aqiBz);

    /**
     * 新增空气质量指数(AQI)级别标准
     * 
     * @param aqiBz 空气质量指数(AQI)级别标准
     * @return 结果
     */
    public int insertAqiBz(AqiBz aqiBz);

    /**
     * 修改空气质量指数(AQI)级别标准
     * 
     * @param aqiBz 空气质量指数(AQI)级别标准
     * @return 结果
     */
    public int updateAqiBz(AqiBz aqiBz);

    /**
     * 批量删除空气质量指数(AQI)级别标准
     * 
     * @param levelIds 需要删除的空气质量指数(AQI)级别标准主键集合
     * @return 结果
     */
    public int deleteAqiBzByLevelIds(Integer[] levelIds);

    /**
     * 删除空气质量指数(AQI)级别标准信息
     * 
     * @param levelId 空气质量指数(AQI)级别标准主键
     * @return 结果
     */
    public int deleteAqiBzByLevelId(Integer levelId);
}
