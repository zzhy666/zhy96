package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.AqiBzMapper;
import com.ruoyi.system.domain.AqiBz;
import com.ruoyi.system.service.IAqiBzService;

/**
 * 空气质量指数(AQI)级别标准Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-22
 */
@Service
public class AqiBzServiceImpl implements IAqiBzService 
{
    @Autowired
    private AqiBzMapper aqiBzMapper;

    /**
     * 查询空气质量指数(AQI)级别标准
     * 
     * @param levelId 空气质量指数(AQI)级别标准主键
     * @return 空气质量指数(AQI)级别标准
     */
    @Override
    public AqiBz selectAqiBzByLevelId(Integer levelId)
    {
        return aqiBzMapper.selectAqiBzByLevelId(levelId);
    }

    /**
     * 查询空气质量指数(AQI)级别标准列表
     * 
     * @param aqiBz 空气质量指数(AQI)级别标准
     * @return 空气质量指数(AQI)级别标准
     */
    @Override
    public List<AqiBz> selectAqiBzList(AqiBz aqiBz)
    {
        return aqiBzMapper.selectAqiBzList(aqiBz);
    }

    /**
     * 新增空气质量指数(AQI)级别标准
     * 
     * @param aqiBz 空气质量指数(AQI)级别标准
     * @return 结果
     */
    @Override
    public int insertAqiBz(AqiBz aqiBz)
    {
        return aqiBzMapper.insertAqiBz(aqiBz);
    }

    /**
     * 修改空气质量指数(AQI)级别标准
     * 
     * @param aqiBz 空气质量指数(AQI)级别标准
     * @return 结果
     */
    @Override
    public int updateAqiBz(AqiBz aqiBz)
    {
        return aqiBzMapper.updateAqiBz(aqiBz);
    }

    /**
     * 批量删除空气质量指数(AQI)级别标准
     * 
     * @param levelIds 需要删除的空气质量指数(AQI)级别标准主键
     * @return 结果
     */
    @Override
    public int deleteAqiBzByLevelIds(Integer[] levelIds)
    {
        return aqiBzMapper.deleteAqiBzByLevelIds(levelIds);
    }

    /**
     * 删除空气质量指数(AQI)级别标准信息
     * 
     * @param levelId 空气质量指数(AQI)级别标准主键
     * @return 结果
     */
    @Override
    public int deleteAqiBzByLevelId(Integer levelId)
    {
        return aqiBzMapper.deleteAqiBzByLevelId(levelId);
    }
}
