package com.example.springbootcreator.service;

import com.example.springbootcreator.entity.Admin;
import com.example.springbootcreator.mapper.AdminMapper;
import com.example.springbootcreator.util.SecurityUtils;
import com.example.springbootcreator.util.WebUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * 用户业务逻辑服务层
 */
@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    /**
     * 新增或更新用户。
     * - 如果是新用户，生成 Salt 并哈希密码。
     * - 如果是更新用户，只有当提供了新密码时才更新密码和 Salt。
     * @param rawPassword 原始密码 (如果是更新操作且不想修改密码，可以传入 null 或空字符串)
     * @return Integer
     */
    @Transactional
    public Boolean save(HttpSession session, Admin saveAdmin, String rawPassword) {
        LocalDateTime now = LocalDateTime.now();

        Boolean isNew = (saveAdmin.getId() == null || saveAdmin.getId() == 0) ? true : false;

        if (isNew) {
            // 新增用户：设置创建时间和默认值
            saveAdmin.setIsRoot(0);
        } else {
            //最高管理员不能被非最高管理员修改
            Admin currentAdmin = (Admin) session.getAttribute(WebUtils.SESSION_CURRENT_ADMIN);
            if (saveAdmin.getIsRoot() == 1 && currentAdmin.getIsRoot() != 1) {
                throw new IllegalArgumentException("Root admin user must be root edit.");
            }
            saveAdmin.setUpdateTime(now);
        }

        // 处理密码：只有在明确提供了新密码时才重新生成 Salt 和哈希
        if (rawPassword != null && !rawPassword.trim().isEmpty()) {
            String newSalt = SecurityUtils.generateSalt();
            String hashedPassword = SecurityUtils.sha1Hash(rawPassword + newSalt);

            if (hashedPassword == null) {
                throw new RuntimeException("Password hashing failed.");
            }

            saveAdmin.setSalt(newSalt);
            saveAdmin.setPassword(hashedPassword);
        } else if (isNew) {
            // 新用户必须提供密码
            throw new IllegalArgumentException("New user must provide a password.");
        }

        Integer affectedRow = 0;

        if (isNew) {
            //新增
            affectedRow = adminMapper.insert(saveAdmin);
        } else {
            //编辑by id
            affectedRow = adminMapper.updateById(saveAdmin);
        }

        if (affectedRow > 0) {
            return true;
        } else  {
            return false;
        }
    }

}
