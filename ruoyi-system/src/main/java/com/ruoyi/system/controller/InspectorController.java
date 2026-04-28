package com.ruoyi.system.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.utils.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.Inspector;
import com.ruoyi.system.service.IInspectorService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import com.ruoyi.common.utils.SecurityUtils;  // 导入密码工具类
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 人员Controller
 * 
 * @author ruoyi
 * @date 2026-04-22
 */

@RestController
@RequestMapping("/system/inspector")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class InspectorController extends BaseController
{
    @Autowired
    private IInspectorService inspectorService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/register")
    public AjaxResult register(@RequestBody Inspector inspector) {

        // 1. 获取并校验参数
        String name = inspector.getName();
        String phone = inspector.getPhone();
        String password = inspector.getPassword();

        if (StringUtils.isBlank(name)) {
            return AjaxResult.error("姓名不能为空");
        }
        if (StringUtils.isBlank(phone)) {
            return AjaxResult.error("手机号不能为空");
        }
        if (StringUtils.isBlank(password)) {
            return AjaxResult.error("密码不能为空");
        }

        // 手机号格式校验 (可选但推荐)
        if (!phone.matches("^1[3-9]\\d{9}$")) {
            return AjaxResult.error("手机号格式不正确");
        }

        // 2. 调用 Service 层的新注册方法
        try {
            int rows = inspectorService.registerInspector(inspector); // 调用新方法
            if (rows > 0) {
                // 插入成功，清除密码后返回
                inspector.setPassword(null);
                return AjaxResult.success("注册成功", inspector);
            } else {
                return AjaxResult.error("注册失败，数据未保存");
            }
        } catch (Exception e) {
            logger.error("注册用户失败，手机号：" + phone, e);
            // 处理业务异常，如手机号重复
            if (e.getMessage().contains("手机号")) {
                return AjaxResult.error(e.getMessage()); // 如"该手机号已注册"
            }
            return AjaxResult.error("注册失败，系统异常");
        }
    }
    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:5173")
    public AjaxResult login(@RequestBody Map<String, String> params) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("=== 登录请求开始 ===");
        System.out.println("时间: " + new Date());

        String phone = params.get("phone");
        String password = params.get("password");

        System.out.println("1. 接收到的原始参数:");
        System.out.println("   手机号: '" + phone + "' (长度: " + (phone != null ? phone.length() : 0) + ")");
        System.out.println("   密码: '" + password + "' (长度: " + (password != null ? password.length() : 0) + ")");

        // 详细分析密码
        if (password != null) {
            System.out.println("2. 密码详细分析:");
            for (int i = 0; i < password.length(); i++) {
                char c = password.charAt(i);
                System.out.println("   字符[" + i + "]: '" + c + "' (ASCII: " + (int)c + ", 十六进制: 0x" + Integer.toHexString(c) + ")");
            }
        }

        try {
            // 查询用户
            String sql = "SELECT id, phone, name, password FROM inspector WHERE phone = ?";
            List<Map<String, Object>> users = jdbcTemplate.queryForList(sql, phone);

            System.out.println("3. 数据库查询结果:");
            System.out.println("   查询到用户数: " + users.size());

            if (users.isEmpty()) {
                System.out.println("❌ 错误: 用户不存在");
                return AjaxResult.error("用户不存在");
            }

            Map<String, Object> user = users.get(0);
            String dbPassword = (String) user.get("password");

            System.out.println("4. 数据库中的用户信息:");
            System.out.println("   用户名: " + user.get("name"));
            System.out.println("   数据库密码: " + dbPassword);
            System.out.println("   密码长度: " + (dbPassword != null ? dbPassword.length() : 0));
            System.out.println("   是BCrypt格式: " + (dbPassword != null && (dbPassword.startsWith("$2a$") || dbPassword.startsWith("$2b$"))));

            // 密码验证
            System.out.println("5. 开始密码验证...");
            boolean passwordMatch = false;

            if (dbPassword != null) {
                // 使用 BCrypt 验证
                try {
                    passwordMatch = BCrypt.checkpw(password, dbPassword);
                    System.out.println("   BCrypt验证结果: " + passwordMatch);

                    if (passwordMatch) {
                        System.out.println("   ✅ BCrypt验证成功!");
                    } else {
                        System.out.println("   ❌ BCrypt验证失败!");
                        System.out.println("      输入密码: '" + password + "'");
                        System.out.println("      数据库密码: '" + dbPassword + "'");
                    }
                } catch (Exception e) {
                    System.out.println("   ❌ BCrypt验证异常: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("   ❌ 数据库密码为空!");
            }

            if (passwordMatch) {
                System.out.println("6. ✅ 登录成功，准备返回数据");
                // 移除敏感信息
                user.remove("password");

                // 生成返回数据
                Map<String, Object> data = new HashMap<>();
                data.put("token", "token_" + System.currentTimeMillis());
                data.put("userInfo", user);

                System.out.println("   返回用户信息: " + user);
                System.out.println("✅ 登录流程完成，返回成功");
                System.out.println("=".repeat(60));
                return AjaxResult.success("登录成功", data);
            } else {
                System.out.println("6. ❌ 登录失败: 密码验证失败");
                System.out.println("   前端发送的密码: '" + password + "'");
                System.out.println("   数据库中的密码: '" + dbPassword + "'");
                System.out.println("=".repeat(60));
                return AjaxResult.error("密码错误");
            }

        } catch (Exception e) {
            System.out.println("❌ 系统异常: " + e.getMessage());
            e.printStackTrace();
            System.out.println("=".repeat(60));
            return AjaxResult.error("系统异常: " + e.getMessage());
        }
    }
    @PostMapping("/reset-password")
    public AjaxResult resetPassword(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String newPassword = params.get("password");

        if (StringUtils.isBlank(phone) || StringUtils.isBlank(newPassword)) {
            return AjaxResult.error("手机号和密码不能为空");
        }

        try {
            String updateSql = "UPDATE inspector SET password = ? WHERE phone = ?";
            int rows = jdbcTemplate.update(updateSql, newPassword, phone);

            if (rows > 0) {
                return AjaxResult.success("密码重置成功");
            } else {
                return AjaxResult.error("用户不存在或更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("重置失败: " + e.getMessage());
        }
    }
    @PostMapping("/direct-login")
    public AjaxResult directLogin(@RequestBody Map<String, String> params) {
        System.out.println("=== 直接登录接口被调用 ===");
        System.out.println("参数：" + params);

        String phone = params.get("phone");
        String password = params.get("password");

        if (StringUtils.isBlank(phone)) {
            return AjaxResult.error("手机号不能为空");
        }
        if (StringUtils.isBlank(password)) {
            return AjaxResult.error("密码不能为空");
        }

        // 直接使用JdbcTemplate查询
        try {
            String sql = "SELECT * FROM inspector WHERE phone = ?";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, phone);
            System.out.println("查询结果数量：" + results.size());

            if (results.isEmpty()) {
                System.out.println("❌ 用户不存在，手机号：" + phone);
                return AjaxResult.error("用户不存在");
            }

            Map<String, Object> user = results.get(0);
            System.out.println("找到用户：" + user);

            // 直接比较密码
            String dbPassword = (String) user.get("password");
            if (dbPassword == null || !password.equals(dbPassword)) {
                System.out.println("❌ 密码错误");
                return AjaxResult.error("密码错误");
            }

            System.out.println("✅ 密码验证成功");

            // 移除密码，不返回给前端
            user.remove("password");

            return AjaxResult.success("登录成功", user);

        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("查询异常：" + e.getMessage());
        }
    }
    @GetMapping("/check-password")
    public AjaxResult checkPasswordFormat() {
        System.out.println("=== 检查密码格式 ===");

        try {
            String sql = "SELECT id, phone, name, password, LENGTH(password) as pwd_length FROM inspector ORDER BY id";
            List<Map<String, Object>> users = jdbcTemplate.queryForList(sql);

            System.out.println("用户密码信息：");
            for (Map<String, Object> user : users) {
                String password = (String) user.get("password");
                System.out.println("  ID: " + user.get("id") +
                        ", 手机号: '" + user.get("phone") +
                        "', 密码: '" + password +
                        "', 密码长度: " + user.get("pwd_length"));
            }

            return AjaxResult.success("查询成功", users);

        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("查询异常：" + e.getMessage());
        }
    }
    /**
     * 查询人员列表
     */
    @PreAuthorize("@ss.hasPermi('system:inspector:list')")
    @GetMapping("/list")
    public TableDataInfo list(Inspector inspector)
    {
        startPage();
        List<Inspector> list = inspectorService.selectInspectorList(inspector);
        return getDataTable(list);
    }

    /**
     * 导出人员列表
     */
    @PreAuthorize("@ss.hasPermi('system:inspector:export')")
    @Log(title = "人员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Inspector inspector)
    {
        List<Inspector> list = inspectorService.selectInspectorList(inspector);
        ExcelUtil<Inspector> util = new ExcelUtil<Inspector>(Inspector.class);
        util.exportExcel(response, list, "人员数据");
    }

    /**
     * 获取人员详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:inspector:query')")
    @GetMapping("/{id:\\d+}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(inspectorService.selectInspectorById(id));
    }

    /**
     * 新增人员
     */
    @PreAuthorize("@ss.hasPermi('system:inspector:add')")
    @Log(title = "人员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Inspector inspector)
    {
        return toAjax(inspectorService.insertInspector(inspector));
    }

    /**
     * 修改人员
     */
    @PreAuthorize("@ss.hasPermi('system:inspector:edit')")
    @Log(title = "人员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Inspector inspector)
    {
        return toAjax(inspectorService.updateInspector(inspector));
    }

    /**
     * 删除人员
     */
    @PreAuthorize("@ss.hasPermi('system:inspector:remove')")
    @Log(title = "人员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(inspectorService.deleteInspectorByIds(ids));
    }
    @GetMapping("/test")
    public AjaxResult test() {
        System.out.println("=== 测试接口被调用 ===");
        System.out.println("时间：" + new Date());
        return AjaxResult.success("测试接口正常工作");
    }
    @GetMapping("/check-db")
    public AjaxResult checkDatabase() {
        try {
            // 1. 检查表是否存在
            String tableExistsSql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'inspector'";
            int tableCount = jdbcTemplate.queryForObject(tableExistsSql, Integer.class);

            if (tableCount == 0) {
                return AjaxResult.error("inspector表不存在");
            }

            // 2. 检查用户数量
            String countSql = "SELECT COUNT(*) FROM inspector";
            int userCount = jdbcTemplate.queryForObject(countSql, Integer.class);

            // 3. 获取所有用户
            String allUsersSql = "SELECT id, phone, name, password FROM inspector";
            List<Map<String, Object>> users = jdbcTemplate.queryForList(allUsersSql);

            Map<String, Object> result = new HashMap<>();
            result.put("tableExists", true);
            result.put("userCount", userCount);
            result.put("users", users);

            // 打印详细信息
            System.out.println("\n=== 数据库检查结果 ===");
            System.out.println("表是否存在: 是");
            System.out.println("用户数量: " + userCount);
            System.out.println("用户列表:");
            for (Map<String, Object> user : users) {
                System.out.println("  ID: " + user.get("id") +
                        ", 手机号: " + user.get("phone") +
                        ", 姓名: " + user.get("name") +
                        ", 密码: " + user.get("password"));
            }

            return AjaxResult.success("数据库检查完成", result);

        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("数据库检查失败: " + e.getMessage());
        }
    }
    @GetMapping("/health")
    public AjaxResult healthCheck() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("timestamp", System.currentTimeMillis());
        data.put("service", "Inspector Service");
        return AjaxResult.success("服务正常", data);
    }
    // 测试接口1：直接查询用户
    @GetMapping("/find-user/{phone}")
    public AjaxResult findUser(@PathVariable String phone) {
        try {
            String sql = "SELECT id, phone, name, password, LENGTH(password) as pwd_len FROM inspector WHERE phone = ?";
            List<Map<String, Object>> users = jdbcTemplate.queryForList(sql, phone);

            if (users.isEmpty()) {
                return AjaxResult.error("用户不存在");
            }

            Map<String, Object> user = users.get(0);
            String password = (String) user.get("password");
            boolean isBcrypt = password != null && (password.startsWith("$2a$") || password.startsWith("$2b$"));

            Map<String, Object> result = new HashMap<>();
            result.put("user", user);
            result.put("isBcrypt", isBcrypt);
            result.put("passwordLength", password != null ? password.length() : 0);

            return AjaxResult.success("查询成功", result);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    // 测试接口2：测试BCrypt密码
    @GetMapping("/test-bcrypt2")
    public AjaxResult testBcrypt2() {
        // 测试密码123456
        String rawPassword = "123456";

        // 生成加密密码
        String encodedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        // 验证
        boolean isMatch = BCrypt.checkpw(rawPassword, encodedPassword);

        Map<String, Object> result = new HashMap<>();
        result.put("rawPassword", rawPassword);
        result.put("encodedPassword", encodedPassword);
        result.put("isMatch", isMatch);
        result.put("encodedLength", encodedPassword.length());
        result.put("startsWith$2a$", encodedPassword.startsWith("$2a$"));

        return AjaxResult.success("BCrypt测试", result);
    }
    @PostMapping("/simple-login-test")
    @CrossOrigin(origins = "http://localhost:5173")
    public AjaxResult simpleLoginTest(@RequestBody Map<String, String> params) {
        System.out.println("=== 简单登录测试接口 ===");
        System.out.println("接收参数: " + params);

        String phone = params.get("phone");
        String password = params.get("password");

        try {
            // 1. 查询用户
            String sql = "SELECT id, phone, name, password FROM inspector WHERE phone = ?";
            List<Map<String, Object>> users = jdbcTemplate.queryForList(sql, phone);

            if (users.isEmpty()) {
                return AjaxResult.error("用户不存在");
            }

            Map<String, Object> user = users.get(0);
            String dbPassword = (String) user.get("password");

            // 2. 验证密码
            boolean isValid = BCrypt.checkpw(password, dbPassword);

            if (isValid) {
                user.remove("password");
                Map<String, Object> data = new HashMap<>();
                data.put("token", "simple_token_" + System.currentTimeMillis());
                data.put("userInfo", user);
                return AjaxResult.success("登录成功", data);
            } else {
                return AjaxResult.error("密码错误");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("系统异常: " + e.getMessage());
        }
    }


}


