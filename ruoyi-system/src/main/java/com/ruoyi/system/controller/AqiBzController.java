package com.ruoyi.system.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.AqiBz;
import com.ruoyi.system.service.IAqiBzService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 空气质量指数(AQI)级别标准Controller
 * 
 * @author ruoyi
 * @date 2026-04-22
 */
@RestController
@RequestMapping("/system/bz")
public class AqiBzController extends BaseController
{
    @Autowired
    private IAqiBzService aqiBzService;

    /**
     * 查询空气质量指数(AQI)级别标准列表
     */
    @PreAuthorize("@ss.hasPermi('system:bz:list')")
    @GetMapping("/list")
    public TableDataInfo list(AqiBz aqiBz)
    {
        startPage();
        List<AqiBz> list = aqiBzService.selectAqiBzList(aqiBz);
        return getDataTable(list);
    }

    /**
     * 导出空气质量指数(AQI)级别标准列表
     */
    @PreAuthorize("@ss.hasPermi('system:bz:export')")
    @Log(title = "空气质量指数(AQI)级别标准", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AqiBz aqiBz)
    {
        List<AqiBz> list = aqiBzService.selectAqiBzList(aqiBz);
        ExcelUtil<AqiBz> util = new ExcelUtil<AqiBz>(AqiBz.class);
        util.exportExcel(response, list, "空气质量指数(AQI)级别标准数据");
    }

    /**
     * 获取空气质量指数(AQI)级别标准详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:bz:query')")
    @GetMapping(value = "/{levelId}")
    public AjaxResult getInfo(@PathVariable("levelId") Integer levelId)
    {
        return success(aqiBzService.selectAqiBzByLevelId(levelId));
    }

    /**
     * 新增空气质量指数(AQI)级别标准
     */
    @PreAuthorize("@ss.hasPermi('system:bz:add')")
    @Log(title = "空气质量指数(AQI)级别标准", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AqiBz aqiBz)
    {
        return toAjax(aqiBzService.insertAqiBz(aqiBz));
    }

    /**
     * 修改空气质量指数(AQI)级别标准
     */
    @PreAuthorize("@ss.hasPermi('system:bz:edit')")
    @Log(title = "空气质量指数(AQI)级别标准", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AqiBz aqiBz)
    {
        return toAjax(aqiBzService.updateAqiBz(aqiBz));
    }

    /**
     * 删除空气质量指数(AQI)级别标准
     */
    @PreAuthorize("@ss.hasPermi('system:bz:remove')")
    @Log(title = "空气质量指数(AQI)级别标准", businessType = BusinessType.DELETE)
	@DeleteMapping("/{levelIds}")
    public AjaxResult remove(@PathVariable Integer[] levelIds)
    {
        return toAjax(aqiBzService.deleteAqiBzByLevelIds(levelIds));
    }
}
