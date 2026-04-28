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
import com.ruoyi.system.domain.AqiFeedback;
import com.ruoyi.system.service.IAqiFeedbackService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 空气质量用户反馈Controller (需登录认证)
 */
@RestController
@RequestMapping("/system/feedback")
public class AqiFeedbackController extends BaseController
{
    @Autowired
    private IAqiFeedbackService aqiFeedbackService;

    /**
     * 查询列表
     */
    @GetMapping("/list")
    public TableDataInfo list(AqiFeedback aqiFeedback)
    {
        startPage();
        List<AqiFeedback> list = aqiFeedbackService.selectAqiFeedbackList(aqiFeedback);
        return getDataTable(list);
    }

    /**
     * 导出
     */
    @PreAuthorize("@ss.hasPermi('system:feedback:export')")
    @Log(title = "空气质量用户反馈", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AqiFeedback aqiFeedback)
    {
        List<AqiFeedback> list = aqiFeedbackService.selectAqiFeedbackList(aqiFeedback);
        ExcelUtil<AqiFeedback> util = new ExcelUtil<AqiFeedback>(AqiFeedback.class);
        util.exportExcel(response, list, "空气质量用户反馈数据");
    }

    /**
     * 获取详情
     */
    @PreAuthorize("@ss.hasPermi('system:feedback:query')")
    @GetMapping(value = "/{feedbackId}")
    public AjaxResult getInfo(@PathVariable("feedbackId") Long feedbackId)
    {
        return success(aqiFeedbackService.selectAqiFeedbackByFeedbackId(feedbackId));
    }

    /**
     * 新增反馈 (需认证版本)
     */
    @PostMapping
    public AjaxResult add(@RequestBody AqiFeedback aqiFeedback)
    {
        return success(aqiFeedbackService.insertAqiFeedback(aqiFeedback));
    }
}