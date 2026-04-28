package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 检测员用户控制器
 * 简化版本
 */
@RestController
@RequestMapping("/system/inspector/user")
@CrossOrigin(origins = "*")
public class InspectorUserController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(InspectorUserController.class);

    // 模拟存储用户
    private static Map<String, Map<String, Object>> USER_STORE = new ConcurrentHashMap<>();
    // 存储token
    private static Map<String, TokenInfo> TOKEN_STORE = new ConcurrentHashMap<>();

    static {
        // 初始化一个测试用户
        Map<String, Object> testUser = new HashMap<>();
        testUser.put("id", 1L);
        testUser.put("phone", "13800138000");
        testUser.put("password", "123456"); // 实际应该加密
        testUser.put("name", "测试用户");
        testUser.put("sex", "0");
        testUser.put("status", "0");
        USER_STORE.put("13800138000", testUser);
    }

    // 注册接口
    @PostMapping("/register")
    public AjaxResult register(@RequestBody Map<String, String> params) {
        try {
            String phone = params.get("phone");
            String name = params.get("name");
            String sex = params.get("sex");
            String password = params.get("password");
            String confirmPassword = params.get("confirmPassword");

            // 参数验证
            if (StringUtils.isAnyBlank(phone, name, password, confirmPassword)) {
                return AjaxResult.error("参数不完整");
            }

            if (!password.equals(confirmPassword)) {
                return AjaxResult.error("两次密码不一致");
            }

            if (phone.length() != 11 || !phone.startsWith("1")) {
                return AjaxResult.error("手机号格式不正确");
            }

            // 检查是否已注册
            if (USER_STORE.containsKey(phone)) {
                return AjaxResult.error("该手机号已注册");
            }

            // 创建用户
            Map<String, Object> newUser = new HashMap<>();
            newUser.put("id", USER_STORE.size() + 1L);
            newUser.put("phone", phone);
            newUser.put("password", password);
            newUser.put("name", name);
            newUser.put("sex", StringUtils.isNotBlank(sex) ? sex : "0");
            newUser.put("status", "0"); // 正常状态

            USER_STORE.put(phone, newUser);

            Map<String, Object> result = new HashMap<>();
            result.put("phone", phone);
            result.put("name", name);
            result.put("message", "注册成功");

            return AjaxResult.success("注册成功", result);

        } catch (Exception e) {
            logger.error("注册失败", e);
            return AjaxResult.error("注册失败");
        }
    }

    // 登录接口
    @PostMapping("/login")
    public AjaxResult login(@RequestBody Map<String, String> params) {
        try {
            String phone = params.get("phone");
            String password = params.get("password");

            if (StringUtils.isAnyBlank(phone, password)) {
                return AjaxResult.error("手机号和密码不能为空");
            }

            // 查找用户
            Map<String, Object> user = USER_STORE.get(phone);
            if (user == null) {
                return AjaxResult.error("用户不存在");
            }

            // 验证密码
            String storedPassword = (String) user.get("password");
            if (!storedPassword.equals(password)) {
                return AjaxResult.error("密码错误");
            }

            // 生成token
            String token = "inspector_" + System.currentTimeMillis();

            // 存储token
            TOKEN_STORE.put(token, new TokenInfo((Long) user.get("id"), phone));

            // 用户信息
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.get("id"));
            userInfo.put("phone", user.get("phone"));
            userInfo.put("name", user.get("name"));
            userInfo.put("sex", user.get("sex"));
            userInfo.put("status", user.get("status"));

            // 返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("userInfo", userInfo);

            return AjaxResult.success("登录成功", result);

        } catch (Exception e) {
            logger.error("登录失败", e);
            return AjaxResult.error("登录失败");
        }
    }

    // 获取网格列表
    @GetMapping("/grid/list")
    public AjaxResult getGridList(@RequestParam String token) {
        try {
            // 验证token
            if (!validateToken(token)) {
                return AjaxResult.error("请先登录");
            }

            // 模拟网格数据
            List<Map<String, Object>> gridList = new ArrayList<>();

            Map<String, Object> grid1 = new HashMap<>();
            grid1.put("id", 1L);
            grid1.put("name", "东部工业区网格");
            grid1.put("code", "GRID001");
            grid1.put("area", "工业区");
            grid1.put("inspectorCount", 3);
            gridList.add(grid1);

            Map<String, Object> grid2 = new HashMap<>();
            grid2.put("id", 2L);
            grid2.put("name", "西部居民区网格");
            grid2.put("code", "GRID002");
            grid2.put("area", "居民区");
            grid2.put("inspectorCount", 2);
            gridList.add(grid2);

            Map<String, Object> grid3 = new HashMap<>();
            grid3.put("id", 3L);
            grid3.put("name", "南部商业区网格");
            grid3.put("code", "GRID003");
            grid3.put("area", "商业区");
            grid3.put("inspectorCount", 1);
            gridList.add(grid3);

            return AjaxResult.success("获取成功", gridList);

        } catch (Exception e) {
            logger.error("获取网格列表失败", e);
            return AjaxResult.error("获取失败");
        }
    }

    // 选择网格
    @PostMapping("/select/grid")
    public AjaxResult selectGrid(@RequestBody Map<String, Object> params) {
        try {
            String token = (String) params.get("token");
            Long gridId = null;

            if (params.get("gridId") instanceof Integer) {
                gridId = ((Integer) params.get("gridId")).longValue();
            } else if (params.get("gridId") instanceof Long) {
                gridId = (Long) params.get("gridId");
            } else {
                return AjaxResult.error("参数错误");
            }

            // 验证token
            if (!validateToken(token)) {
                return AjaxResult.error("请先登录");
            }

            TokenInfo tokenInfo = TOKEN_STORE.get(token);
            Long userId = tokenInfo.getUserId();

            // 查找用户
            Map<String, Object> user = null;
            for (Map<String, Object> u : USER_STORE.values()) {
                if (userId.equals(u.get("id"))) {
                    user = u;
                    break;
                }
            }

            if (user != null) {
                user.put("gridId", gridId);
                return AjaxResult.success("网格选择成功");
            } else {
                return AjaxResult.error("用户不存在");
            }

        } catch (Exception e) {
            logger.error("选择网格失败", e);
            return AjaxResult.error("选择失败");
        }
    }

    // 提交反馈
    @PostMapping("/feedback/submit")
    public AjaxResult submitFeedback(@RequestBody Map<String, Object> params) {
        try {
            String token = (String) params.get("token");
            Long gridId = null;

            if (params.get("gridId") instanceof Integer) {
                gridId = ((Integer) params.get("gridId")).longValue();
            } else if (params.get("gridId") instanceof Long) {
                gridId = (Long) params.get("gridId");
            }

            Double pm25 = getDoubleParam(params, "pm25");
            Double pm10 = getDoubleParam(params, "pm10");
            String description = (String) params.get("description");

            // 验证token
            if (!validateToken(token)) {
                return AjaxResult.error("请先登录");
            }

            // 简单计算AQI
            int aqi = 0;
            if (pm25 != null && pm10 != null) {
                aqi = (int) ((pm25 + pm10) / 2);
            }

            String qualityLevel = "良好";
            if (aqi > 100) {
                qualityLevel = "轻度污染";
            } else if (aqi > 150) {
                qualityLevel = "中度污染";
            } else if (aqi > 200) {
                qualityLevel = "重度污染";
            }

            Map<String, Object> result = new HashMap<>();
            result.put("aqi", aqi);
            result.put("qualityLevel", qualityLevel);
            result.put("message", "反馈提交成功");

            return AjaxResult.success("反馈提交成功", result);

        } catch (Exception e) {
            logger.error("提交反馈失败", e);
            return AjaxResult.error("提交失败");
        }
    }

    // 获取反馈历史
    @GetMapping("/feedback/history")
    public AjaxResult getFeedbackHistory(@RequestParam String token) {
        try {
            if (!validateToken(token)) {
                return AjaxResult.error("请先登录");
            }

            // 模拟反馈数据
            List<Map<String, Object>> history = new ArrayList<>();

            Map<String, Object> feedback1 = new HashMap<>();
            feedback1.put("id", 1L);
            feedback1.put("gridName", "东部工业区网格");
            feedback1.put("aqi", 65);
            feedback1.put("qualityLevel", "良好");
            feedback1.put("pm25", 30.5);
            feedback1.put("pm10", 45.2);
            feedback1.put("createTime", "2024-01-15 10:30:00");
            history.add(feedback1);

            Map<String, Object> feedback2 = new HashMap<>();
            feedback2.put("id", 2L);
            feedback2.put("gridName", "西部居民区网格");
            feedback2.put("aqi", 85);
            feedback2.put("qualityLevel", "良好");
            feedback2.put("pm25", 40.2);
            feedback2.put("pm10", 52.1);
            feedback2.put("createTime", "2024-01-14 15:20:00");
            history.add(feedback2);

            return AjaxResult.success("获取成功", history);

        } catch (Exception e) {
            logger.error("获取历史失败", e);
            return AjaxResult.error("获取失败");
        }
    }

    // 验证token
    private boolean validateToken(String token) {
        if (StringUtils.isBlank(token)) {
            return false;
        }
        TokenInfo tokenInfo = TOKEN_STORE.get(token);
        return tokenInfo != null && tokenInfo.isValid();
    }

    // 获取double类型参数
    private Double getDoubleParam(Map<String, Object> params, String key) {
        Object value = params.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    // Token信息类
    static class TokenInfo {
        private Long userId;
        private String phone;
        private long createTime;

        public TokenInfo(Long userId, String phone) {
            this.userId = userId;
            this.phone = phone;
            this.createTime = System.currentTimeMillis();
        }

        public Long getUserId() { return userId; }
        public String getPhone() { return phone; }

        public boolean isValid() {
            // token有效期为24小时
            return (System.currentTimeMillis() - createTime) < 24 * 60 * 60 * 1000;
        }
    }
}