package com.ruoyi.system.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.ProvinceCity;
import com.ruoyi.system.service.IProvinceCityService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.annotation.Anonymous;

/**
 * 【请填写功能名称】Controller
 * 
 * @author ruoyi
 * @date 2026-04-25
 */
@RestController
@RequestMapping("/system/city")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class ProvinceCityController extends BaseController
{
    @Autowired
    private IProvinceCityService provinceCityService;

    /**
     * 查询【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:city:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProvinceCity provinceCity)
    {
        startPage();
        List<ProvinceCity> list = provinceCityService.selectProvinceCityList(provinceCity);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:city:export')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProvinceCity provinceCity)
    {
        List<ProvinceCity> list = provinceCityService.selectProvinceCityList(provinceCity);
        ExcelUtil<ProvinceCity> util = new ExcelUtil<ProvinceCity>(ProvinceCity.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:city:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(provinceCityService.selectProvinceCityById(id));
    }

    /**
     * 新增【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:city:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProvinceCity provinceCity)
    {
        return toAjax(provinceCityService.insertProvinceCity(provinceCity));
    }

    /**
     * 修改【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:city:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProvinceCity provinceCity)
    {
        return toAjax(provinceCityService.updateProvinceCity(provinceCity));
    }

    /**
     * 删除【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:city:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(provinceCityService.deleteProvinceCityByIds(ids));
    }
    /**
     * 获取所有省份（无权限控制，供检测员前端使用）
     */
    @Anonymous
    @GetMapping("/provinces")
    public AjaxResult getAllProvinces() {
        try {
            // 创建一个查询条件，只查询省份（level=1）
            ProvinceCity query = new ProvinceCity();
            query.setLevel(1L);  // 改为 1L，表示 Long 类型

            // 不进行分页，获取所有数据
            List<ProvinceCity> provinces = provinceCityService.selectProvinceCityList(query);
            return AjaxResult.success("获取省份列表成功", provinces);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("获取省份列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据省份ID获取城市（无权限控制，供检测员前端使用）
     */
    @Anonymous
    @GetMapping("/cities/{provinceId}")
    public AjaxResult getCitiesByProvinceId(@PathVariable Long provinceId) {
        try {
            // 创建一个查询条件，查询指定省份下的城市
            ProvinceCity query = new ProvinceCity();
            query.setParentId(provinceId);  // 父级ID是省份ID
            query.setLevel(2L);  // 改为 2L，表示 Long 类型

            // 不进行分页，获取所有数据
            List<ProvinceCity> cities = provinceCityService.selectProvinceCityList(query);
            return AjaxResult.success("获取城市列表成功", cities);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("获取城市列表失败: " + e.getMessage());
        }
    }
}
