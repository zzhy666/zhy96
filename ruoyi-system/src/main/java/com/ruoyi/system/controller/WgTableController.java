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
import com.ruoyi.system.domain.WgTable;
import com.ruoyi.system.service.IWgTableService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 网格人员Controller
 * 
 * @author ruoyi
 * @date 2026-04-23
 */
@RestController
@RequestMapping("/system/table")
public class WgTableController extends BaseController
{
    @Autowired
    private IWgTableService wgTableService;

    /**
     * 查询网格人员列表
     */
    @PreAuthorize("@ss.hasPermi('system:table:list')")
    @GetMapping("/list")
    public TableDataInfo list(WgTable wgTable)
    {
        startPage();
        List<WgTable> list = wgTableService.selectWgTableList(wgTable);
        return getDataTable(list);
    }

    /**
     * 导出网格人员列表
     */
    @PreAuthorize("@ss.hasPermi('system:table:export')")
    @Log(title = "网格人员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WgTable wgTable)
    {
        List<WgTable> list = wgTableService.selectWgTableList(wgTable);
        ExcelUtil<WgTable> util = new ExcelUtil<WgTable>(WgTable.class);
        util.exportExcel(response, list, "网格人员数据");
    }

    /**
     * 获取网格人员详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:table:query')")
    @GetMapping(value = "/{wgId}")
    public AjaxResult getInfo(@PathVariable("wgId") Long wgId)
    {
        return success(wgTableService.selectWgTableByWgId(wgId));
    }

    /**
     * 新增网格人员
     */
    @PreAuthorize("@ss.hasPermi('system:table:add')")
    @Log(title = "网格人员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WgTable wgTable)
    {
        return toAjax(wgTableService.insertWgTable(wgTable));
    }

    /**
     * 修改网格人员
     */
    @PreAuthorize("@ss.hasPermi('system:table:edit')")
    @Log(title = "网格人员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WgTable wgTable)
    {
        return toAjax(wgTableService.updateWgTable(wgTable));
    }

    /**
     * 删除网格人员
     */
    @PreAuthorize("@ss.hasPermi('system:table:remove')")
    @Log(title = "网格人员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{wgIds}")
    public AjaxResult remove(@PathVariable Long[] wgIds)
    {
        return toAjax(wgTableService.deleteWgTableByWgIds(wgIds));
    }
}
