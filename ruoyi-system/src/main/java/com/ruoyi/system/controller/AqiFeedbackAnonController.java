package com.ruoyi.system.controller;
import com.ruoyi.system.domain.AqiBz;
import com.ruoyi.system.domain.CheckRecord;
import com.ruoyi.system.domain.Inspector;
import com.ruoyi.system.service.IAqiBzService;
import com.ruoyi.system.service.IInspectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.AqiFeedback;
import com.ruoyi.system.service.IAqiFeedbackService;

import java.text.SimpleDateFormat;
import java.util.List;
import com.ruoyi.system.service.ICheckRecordService;
import java.util.Date;

/**
 * 空气质量反馈匿名访问控制器
 * 此控制器专门处理不需要认证的反馈接口
 */
@RestController
@RequestMapping("/anon/feedback")
public class AqiFeedbackAnonController {

    @Autowired
    private IAqiFeedbackService aqiFeedbackService;
    @Autowired
    private ICheckRecordService checkRecordService;
    @Autowired
    private IInspectorService inspectorService;
    @Autowired
    private IAqiBzService aqiBzService;

    /**
     * 获取反馈列表 - 匿名访问
     * 访问路径：GET http://localhost:8080/anon/feedback/list
     */
    @GetMapping("/list")
    public AjaxResult list() {
        try {
            System.out.println("=== 收到 /anon/feedback/list 请求 ===");
            List<AqiFeedback> list = aqiFeedbackService.selectAqiFeedbackList(new AqiFeedback());
            System.out.println("查询到数据数量：" + list.size());
            return AjaxResult.success("获取成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 提交反馈 - 匿名访问
     * 访问路径：POST http://localhost:8080/anon/feedback
     */
    @PostMapping
    public AjaxResult add(@RequestBody AqiFeedback aqiFeedback) {
        try {

            if (aqiFeedback.getInspectorName() == null || aqiFeedback.getInspectorName().trim().isEmpty()) {
                Inspector inspector = inspectorService.selectInspectorById(aqiFeedback.getInspectorId());
                if (inspector != null && inspector.getName() != null) {
                    // 使用正确的 getName() 方法
                    aqiFeedback.setInspectorName(inspector.getName());
                    System.out.println("=== 从数据库获取检测员姓名: " + inspector.getName() + " ===");
                } else {
                    // 如果数据库中查不到，使用前端传递的值或默认值
                    System.out.println("=== 检测员信息为空，使用备用名称 ===");
                }
            }

            // 设置反馈时间
            aqiFeedback.setFeedbackTime(new Date());

            int result = aqiFeedbackService.insertAqiFeedback(aqiFeedback);

            if (result > 0) {
                System.out.println("=== 成功保存到 aqi_feedback 表 ===");

                // 保存到 check_record 表
                CheckRecord checkRecord = new CheckRecord();

                // 设置检测员信息
                checkRecord.setInspectorId(aqiFeedback.getInspectorId());
                checkRecord.setInspectorName(aqiFeedback.getInspectorName());

                // 设置地点
                StringBuilder location = new StringBuilder();
                if (aqiFeedback.getProvinceName() != null && !aqiFeedback.getProvinceName().trim().isEmpty()) {
                    location.append(aqiFeedback.getProvinceName().trim());
                }

                // 拼接城市
                if (aqiFeedback.getCityName() != null && !aqiFeedback.getCityName().trim().isEmpty()) {
                    location.append(aqiFeedback.getCityName().trim());
                }

                // 拼接详细地址
                if (aqiFeedback.getDetailAddress() != null && !aqiFeedback.getDetailAddress().trim().isEmpty()) {
                    location.append(aqiFeedback.getDetailAddress().trim());
                }
                //如果地址为空，设置默认值
                if (location.length() == 0) {
                    checkRecord.setLocation("未知地点");
                } else {
                    checkRecord.setLocation(location.toString());
                }
                if (aqiFeedback.getAirDescription() != null && !aqiFeedback.getAirDescription().trim().isEmpty()) {
                    // 只放描述，不要拼接省份城市
                    checkRecord.setAdditionalData(aqiFeedback.getAirDescription().trim());
                } else {
                    checkRecord.setAdditionalData("无");
                }

                if (aqiFeedback.getAqiLevelId() != null) {
                    checkRecord.setAqiLevelId(aqiFeedback.getAqiLevelId());

                    // 可选：记录日志查看 AQI 级别信息
                    AqiBz aqiBz = aqiBzService.selectAqiBzByLevelId(aqiFeedback.getAqiLevelId());
                    if (aqiBz != null) {
                        System.out.println("获取到的 AQI 级别信息: " + aqiBz.getAqiLevel());
                    }
                } else {
                    checkRecord.setAqiLevelId(0);  // 或者 null，根据数据库设计
                }

                // 设置时间
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                Date now = new Date();

                checkRecord.setCheckDate(dateFormat.format(now));
                checkRecord.setCheckTime(timeFormat.format(now));
                checkRecord.setSubmitTime(dateFormat.format(now));

                // 设置反馈ID关联
                checkRecord.setFeedbackId(aqiFeedback.getFeedbackId());

                // 设置状态
                checkRecord.setDispatchStatus("未派遣");
                checkRecord.setDetectStatus("正常");

                // 额外数据


                System.out.println("=== 准备插入 check_record 表的数据 ===");
                System.out.println("检测员ID: " + checkRecord.getInspectorId());
                System.out.println("检测员姓名: " + checkRecord.getInspectorName());
                System.out.println("地点: " + checkRecord.getLocation());
                System.out.println("AQI级别ID: " + checkRecord.getAqiLevelId());
                System.out.println("反馈ID: " + checkRecord.getFeedbackId());

                int checkResult = checkRecordService.insertCheckRecord(checkRecord);
                System.out.println("插入check_record表结果：" + checkResult);

                if (checkResult > 0) {
                    System.out.println("=== 成功保存到 check_record 表 ===");
                    return AjaxResult.success("反馈提交成功", result);
                } else {
                    System.out.println("=== 保存到 check_record 表失败 ===");
                    // 改为只返回错误，不尝试删除
                    return AjaxResult.error("检查记录保存失败，但反馈已提交");
                }

            } else {
                return AjaxResult.error("反馈提交失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("服务器错误: " + e.getMessage());
        }
    }
}
