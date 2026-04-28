package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.CheckRecord;
import com.ruoyi.system.service.ICheckRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/system/inspector")
public class InspectorDataReceiverController extends BaseController {

    @Autowired
    private ICheckRecordService checkRecordService;

    // 添加健康检查接口
    @GetMapping("/health-check")
    public AjaxResult healthCheck() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("service", "检测员数据接收服务");
        result.put("timestamp", new Date());
        return AjaxResult.success("服务正常", result);
    }

    @PostMapping("/submit")
    public AjaxResult submitData(@RequestBody Map<String, Object> data) {
        try {
            CheckRecord record = new CheckRecord();

            // 1. 检测员ID
            if (data.get("userId") != null) {
                try {
                    record.setInspectorId(Long.parseLong(data.get("userId").toString()));
                } catch (Exception e) {
                    record.setInspectorId(9999L);
                }
            } else {
                record.setInspectorId(9999L);
            }

            // 2. 检测员姓名
            if (data.get("userName") != null) {
                record.setInspectorName(data.get("userName").toString());
            } else {
                record.setInspectorName("未知检测员");
            }

            // 3. 检测日期 - 实体类中是String，所以直接传递字符串
            if (data.get("checkDate") != null) {
                // 直接传递字符串，不需要转换为Date
                record.setCheckDate(data.get("checkDate").toString());
            } else {
                // 如果没有提供检测日期，使用当前日期字符串
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                record.setCheckDate(dateFormat.format(new Date()));
            }

            // 4. 检测时间 - 实体类中是String，所以直接传递字符串
            if (data.get("checkTime") != null) {
                // 直接传递字符串
                record.setCheckTime(data.get("checkTime").toString());
            } else {
                // 如果没有提供检测时间，使用当前时间字符串
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                record.setCheckTime(timeFormat.format(new Date()));
            }

            // 5. 检测地点
            if (data.get("location") != null) {
                record.setLocation(data.get("location").toString());
            } else {
                record.setLocation("未指定地点");
            }

            // 6. 提交时间 - 实体类中是String，所以使用字符串格式
            SimpleDateFormat submitTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            record.setSubmitTime(submitTimeFormat.format(new Date()));

            // 7. AQI级别ID - 这里需要根据您的业务逻辑设置
            // 注意：实体类中是aqiLevelId(Integer)，不是aqiLevel(String)
            if (data.get("aqiLevel") != null) {
                // 如果前端传的是aqiLevel字符串，需要转换为对应的ID
                String aqiLevel = data.get("aqiLevel").toString();
                Integer aqiLevelId = convertAqiLevelToId(aqiLevel);
                record.setAqiLevelId(aqiLevelId);
            } else if (data.get("aqi") != null) {
                // 如果传的是AQI数值，先计算级别，再转ID
                try {
                    int aqi = Integer.parseInt(data.get("aqi").toString());
                    String aqiLevel = getAqiLevel(aqi);
                    Integer aqiLevelId = convertAqiLevelToId(aqiLevel);
                    record.setAqiLevelId(aqiLevelId);
                } catch (Exception e) {
                    // 解析失败，不设置
                }
            }

            // 8. 附加数据
            if (data.get("additionalData") != null) {
                record.setAdditionalData(data.get("additionalData").toString());
            } else {
                // 如果没有提供附加数据，可以将整个data转换为字符串存储
                record.setAdditionalData(data.toString());
            }

            // 9. 设置创建信息
            record.setCreateBy("inspector_system");
            record.setCreateTime(new Date());  // 这个字段来自BaseEntity，是Date类型

            // 10. 保存到数据库
            int rows = checkRecordService.insertCheckRecord(record);

            if (rows > 0) {
                return AjaxResult.success("数据保存成功");
            } else {
                return AjaxResult.error("数据保存失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("处理错误: " + e.getMessage());
        }
    }

    /**
     * 根据AQI值获取空气质量级别字符串
     */
    private String getAqiLevel(int aqi) {
        if (aqi <= 50) return "优";
        if (aqi <= 100) return "良";
        if (aqi <= 150) return "轻度污染";
        if (aqi <= 200) return "中度污染";
        if (aqi <= 300) return "重度污染";
        return "严重污染";
    }

    /**
     * 将AQI级别字符串转换为ID
     * 注意：这里需要根据您的aqi_bz表中的实际ID进行映射
     */
    private Integer convertAqiLevelToId(String aqiLevel) {
        if ("优".equals(aqiLevel)) return 1;
        if ("良".equals(aqiLevel)) return 2;
        if ("轻度污染".equals(aqiLevel)) return 3;
        if ("中度污染".equals(aqiLevel)) return 4;
        if ("重度污染".equals(aqiLevel)) return 5;
        if ("严重污染".equals(aqiLevel)) return 6;
        return 0; // 未知
    }
}