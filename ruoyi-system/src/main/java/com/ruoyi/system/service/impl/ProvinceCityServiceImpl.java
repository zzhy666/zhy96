package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ProvinceCityMapper;
import com.ruoyi.system.domain.ProvinceCity;
import com.ruoyi.system.service.IProvinceCityService;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-25
 */
@Service
public class ProvinceCityServiceImpl implements IProvinceCityService 
{
    @Autowired
    private ProvinceCityMapper provinceCityMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public ProvinceCity selectProvinceCityById(Long id)
    {
        return provinceCityMapper.selectProvinceCityById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param provinceCity 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<ProvinceCity> selectProvinceCityList(ProvinceCity provinceCity)
    {
        return provinceCityMapper.selectProvinceCityList(provinceCity);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param provinceCity 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertProvinceCity(ProvinceCity provinceCity)
    {
        return provinceCityMapper.insertProvinceCity(provinceCity);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param provinceCity 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateProvinceCity(ProvinceCity provinceCity)
    {
        return provinceCityMapper.updateProvinceCity(provinceCity);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteProvinceCityByIds(Long[] ids)
    {
        return provinceCityMapper.deleteProvinceCityByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteProvinceCityById(Long id)
    {
        return provinceCityMapper.deleteProvinceCityById(id);
    }
}
