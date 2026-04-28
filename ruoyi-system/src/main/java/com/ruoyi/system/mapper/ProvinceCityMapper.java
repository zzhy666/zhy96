package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.ProvinceCity;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-25
 */
public interface ProvinceCityMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public ProvinceCity selectProvinceCityById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param provinceCity 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<ProvinceCity> selectProvinceCityList(ProvinceCity provinceCity);

    /**
     * 新增【请填写功能名称】
     * 
     * @param provinceCity 【请填写功能名称】
     * @return 结果
     */
    public int insertProvinceCity(ProvinceCity provinceCity);

    /**
     * 修改【请填写功能名称】
     * 
     * @param provinceCity 【请填写功能名称】
     * @return 结果
     */
    public int updateProvinceCity(ProvinceCity provinceCity);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteProvinceCityById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProvinceCityByIds(Long[] ids);
}
