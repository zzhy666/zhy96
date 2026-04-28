package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.mapper.InspectorMapper;
import com.ruoyi.system.domain.Inspector;
import com.ruoyi.system.service.IInspectorService;
// 需要导入以下工具类
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import java.util.List;

/**
 * 人员Service业务层处理
 *
 * @author ruoyi
 * @date 2026-04-22
 */
@Service
public class InspectorServiceImpl implements IInspectorService {
    @Autowired
    private InspectorMapper inspectorMapper;

    /**
     * 查询人员
     *
     * @param id 人员主键
     * @return 人员
     */
    @Override
    public Inspector selectInspectorById(Long id) {
        return inspectorMapper.selectInspectorById(id);
    }

    /**
     * 查询人员列表
     *
     * @param inspector 人员
     * @return 人员
     */
    @Override
    public List<Inspector> selectInspectorList(Inspector inspector) {
        return inspectorMapper.selectInspectorList(inspector);
    }

    /**
     * 新增人员
     *
     * @param inspector 人员
     * @return 结果
     */
    @Override
    public int insertInspector(Inspector inspector) {
        return inspectorMapper.insertInspector(inspector);
    }

    /**
     * 修改人员
     *
     * @param inspector 人员
     * @return 结果
     */
    @Override
    public int updateInspector(Inspector inspector) {
        return inspectorMapper.updateInspector(inspector);
    }

    /**
     * 批量删除人员
     *
     * @param ids 需要删除的人员主键
     * @return 结果
     */
    @Override
    public int deleteInspectorByIds(Long[] ids) {
        return inspectorMapper.deleteInspectorByIds(ids);
    }

    /**
     * 删除人员信息
     *
     * @param id 人员主键
     * @return 结果
     */
    @Override
    public int deleteInspectorById(Long id) {
        return inspectorMapper.deleteInspectorById(id);
    }

    /**
     * 注册检测员
     *
     * @param inspector 检测员信息
     * @return 结果
     */
    @Override
    @Transactional
    public int registerInspector(Inspector inspector) {
        // 1. 检查手机号是否已存在
        Inspector existInspector = inspectorMapper.selectInspectorByPhone(inspector.getPhone());
        if (existInspector != null) {
            throw new RuntimeException("该手机号已注册");
        }

        // 2. 对密码进行加密
        if (StringUtils.isNotEmpty(inspector.getPassword())) {
            inspector.setPassword(SecurityUtils.encryptPassword(inspector.getPassword()));
        }


        // 4. 插入数据库
        return inspectorMapper.insertInspector(inspector);
    }
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * 检测员登录
     *
     * @param phone    手机号
     * @param password 密码
     * @return 检测员信息
     */
    @Override
    public Inspector login(String phone, String password) {
        System.out.println("\n=== 登录方法开始 ===");
        System.out.println("收到登录请求 - 手机号: " + phone);

        if (StringUtils.isEmpty(password)) {
            System.out.println("⚠️ 警告：密码为空！");
        }

        // 1. 首先清理和标准化手机号
        String cleanPhone = cleanPhoneNumber(phone);
        System.out.println("清理后手机号: '" + cleanPhone + "'");
        System.out.println("长度: " + cleanPhone.length());

        // 2. 尝试多种方式查询用户
        Inspector inspector = findInspectorByPhone(cleanPhone);

        if (inspector == null) {
            System.out.println("❌ 所有查询方式都未找到用户");
            return null;
        }

        System.out.println("✅ 找到用户: ID=" + inspector.getId() + ", 姓名=" + inspector.getName());

        // 3. 验证密码
        if (StringUtils.isEmpty(password)) {
            System.out.println("❌ 密码为空，无法验证");
            return null;
        }

        System.out.println("验证密码...");
        System.out.println("输入的密码: " + password);
        System.out.println("数据库中的密码: " + (inspector.getPassword() != null ? "[" + inspector.getPassword().length() + " 字符]" : "null"));

        if (SecurityUtils.matchesPassword(password, inspector.getPassword())) {
            System.out.println("✅ 密码验证成功");
            inspector.setPassword(null);
            return inspector;
        }

        System.out.println("❌ 密码验证失败");
        return null;
    }

    /**
     * 清理手机号
     */
    private String cleanPhoneNumber(String phone) {
        if (phone == null) return "";
        return phone.trim();
    }

    /**
     * 尝试多种方式查找用户
     */
    private Inspector findInspectorByPhone(String phone) {
        Inspector inspector = null;

        // 方式1: 使用Mapper精确查询
        System.out.println("\n[方式1] 使用Mapper精确查询: '" + phone + "'");
        inspector = inspectorMapper.selectInspectorByPhone(phone);
        if (inspector != null) {
            System.out.println("✅ 方式1找到用户");
            return inspector;
        }
        System.out.println("❌ 方式1未找到用户");

        // 方式2: 使用JdbcTemplate精确查询
        System.out.println("\n[方式2] 使用JdbcTemplate精确查询");
        try {
            String sql = "SELECT * FROM inspector WHERE phone = ?";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, phone);
            System.out.println("查询结果数: " + results.size());

            if (!results.isEmpty()) {
                System.out.println("✅ JdbcTemplate找到记录: " + results.get(0));

                // 手动构建Inspector对象
                Map<String, Object> row = results.get(0);
                inspector = new Inspector();
                inspector.setId(((Number) row.get("id")).longValue());
                inspector.setName((String) row.get("name"));
                inspector.setPhone((String) row.get("phone"));
                inspector.setPassword((String) row.get("password"));
                return inspector;
            }
        } catch (Exception e) {
            System.out.println("❌ JdbcTemplate查询异常: " + e.getMessage());
        }

        // 方式3: 使用LIKE模糊查询
        System.out.println("\n[方式3] 使用LIKE模糊查询: '%" + phone + "%'");
        try {
            String sql = "SELECT * FROM inspector WHERE phone LIKE ?";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, "%" + phone + "%");
            System.out.println("LIKE查询结果数: " + results.size());

            for (Map<String, Object> row : results) {
                System.out.println("记录: id=" + row.get("id") +
                        ", phone='" + row.get("phone") + "'" +
                        ", name=" + row.get("name"));
            }

            if (!results.isEmpty()) {
                Map<String, Object> row = results.get(0);
                inspector = new Inspector();
                inspector.setId(((Number) row.get("id")).longValue());
                inspector.setName((String) row.get("name"));
                inspector.setPhone((String) row.get("phone"));
                inspector.setPassword((String) row.get("password"));
                return inspector;
            }
        } catch (Exception e) {
            System.out.println("❌ LIKE查询异常: " + e.getMessage());
        }

        // 方式4: 查询所有用户，看看数据库中有什么
        System.out.println("\n[方式4] 查看数据库中所有用户");
        try {
            String sql = "SELECT id, name, phone FROM inspector ORDER BY id";
            List<Map<String, Object>> allUsers = jdbcTemplate.queryForList(sql);

            System.out.println("数据库中共有 " + allUsers.size() + " 个用户:");
            for (Map<String, Object> user : allUsers) {
                String dbPhone = (String) user.get("phone");
                boolean isMatch = dbPhone != null && dbPhone.equals(phone);
                String matchFlag = isMatch ? " ★ 匹配" : "";

                System.out.println(String.format("  ID:%2d, 手机号:'%s', 姓名:%s%s",
                        user.get("id"), dbPhone, user.get("name"), matchFlag));
            }
        } catch (Exception e) {
            System.out.println("❌ 查询所有用户异常: " + e.getMessage());
        }

        return null;
    }
}