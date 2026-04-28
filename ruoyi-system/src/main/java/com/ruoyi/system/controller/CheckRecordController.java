package com.ruoyi.system.controller;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.vo.CheckRecordVO;
import com.ruoyi.system.service.IInspectorService;
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
import com.ruoyi.system.domain.CheckRecord;
import com.ruoyi.system.service.ICheckRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// 在 import 部分添加以下两行
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 检查记录提交Controller
 * 
 * @author ruoyi
 * @date 2026-04-22
 */
@RestController
@RequestMapping("/system/record")
public class CheckRecordController extends BaseController
{
    @Autowired
    private ICheckRecordService checkRecordService;
    @Autowired
    private IInspectorService inspectorService;
    private static final Logger log = LoggerFactory.getLogger(CheckRecordController.class);


    @PostMapping("/dispatch")
    public AjaxResult dispatch(@RequestBody Map<String, Object> params) {
        try {
            Long recordId = params.get("recordId") != null ? Long.parseLong(params.get("recordId").toString()) : null;
            Long inspectorId = params.get("inspectorId") != null ? Long.parseLong(params.get("inspectorId").toString()) : null;

            if (recordId == null) {
                return AjaxResult.error("记录ID不能为空");
            }
            if (inspectorId == null) {
                return AjaxResult.error("网格员ID不能为空");
            }

            // 调用Service层进行派遣操作
            int result = checkRecordService.dispatchCheckRecord(recordId, inspectorId);

            if (result > 0) {
                return AjaxResult.success("派遣成功");
            } else {
                return AjaxResult.error("派遣失败，可能记录不存在或网格员不存在");
            }
        } catch (NumberFormatException e) {
            return AjaxResult.error("参数格式错误");
        } catch (Exception e) {
            log.error("派遣失败: {}", e.getMessage());
            return AjaxResult.error("派遣操作异常");
        }
    }
    /**
     * 查询检查记录提交列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(CheckRecordVO checkRecordVO) {
        startPage();
        List<CheckRecordVO> list = checkRecordService.selectCheckRecordVOList(checkRecordVO);
        return getDataTable(list);
    }

    /**
     * 导出检查记录提交列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:export')")
    @Log(title = "检查记录提交", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CheckRecordVO checkRecordVO)
    {
        List<CheckRecordVO> list = checkRecordService.selectCheckRecordVOList(checkRecordVO);
        ExcelUtil<CheckRecordVO> util = new ExcelUtil<>(CheckRecordVO.class);
        util.exportExcel(response, list, "检查记录提交数据");
    }

    /**
     * 获取检查记录提交详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:record:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId)
    {
        // 改为返回包含关联信息的VO
        CheckRecordVO checkRecordVO = checkRecordService.selectCheckRecordVOByRecordId(recordId);
        return success(checkRecordVO);
    }

    /**
     * 新增检查记录提交
     */
    @PreAuthorize("@ss.hasPermi('system:record:add')")
    @Log(title = "检查记录提交", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CheckRecord checkRecord)
    {
        return toAjax(checkRecordService.insertCheckRecord(checkRecord));
    }

    /**
     * 修改检查记录提交
     */
    @PreAuthorize("@ss.hasPermi('system:record:edit')")
    @Log(title = "检查记录提交", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CheckRecord checkRecord)
    {
        return toAjax(checkRecordService.updateCheckRecord(checkRecord));
    }

    /**
     * 删除检查记录提交
     */
    @PreAuthorize("@ss.hasPermi('system:record:remove')")
    @Log(title = "检查记录提交", businessType = BusinessType.DELETE)
	@DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        return toAjax(checkRecordService.deleteCheckRecordByRecordIds(recordIds));
    }

}

