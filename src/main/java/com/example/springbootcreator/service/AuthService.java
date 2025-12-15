package com.example.springbootcreator.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springbootcreator.entity.Admin;
import com.example.springbootcreator.mapper.AdminMapper;
import com.example.springbootcreator.util.SecurityUtils;
import com.example.springbootcreator.util.WebUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 认证服务, 业务逻辑层, 负责用户登录验证逻辑。
 */
//@Service 就是用于标注业务逻辑处理类的注解, @Component子类别
@Service
public class AuthService {

    @Autowired
    private AdminMapper adminMapper;

    /**
     * 将明文密码转换为 sha-1
     */
    private String hashPassword(String password, String salt) {
        // 拼接密码和盐值进行哈希
        String saltedPassword = password + salt;

        return SecurityUtils.sha1Hash(saltedPassword);
    }

    /**
     * 验证用户凭证。
     * @param username 用户名
     * @param password 明文密码
     * @return 验证成功的 Admin 对象
     */
    public Admin authenticate(String username, String password) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);

        Admin admin = adminMapper.selectOne(queryWrapper);

        if (admin != null) {
            String salt = admin.getSalt();
            String hashedInputPassword = hashPassword(password, salt);

            if (!hashedInputPassword.isEmpty()
                    && hashedInputPassword.equals(admin.getPassword())
                    && admin.getStatus() == 1) {
                return admin;
            }
        }

        return null;
    }

    public Boolean hasPermission(Integer targetPermissionId) {
        if (targetPermissionId == null || targetPermissionId == 0) {
            return false;
        }

        HttpSession session = WebUtils.getCurrentSession();

        if (session == null) {
            return false;
        }

        Admin currentAdmin = (Admin) session.getAttribute(WebUtils.SESSION_CURRENT_ADMIN);
        //最高管理员默认为true
        if (currentAdmin != null && currentAdmin.getIsRoot() !=null && currentAdmin.getIsRoot() == 1) {
            return true;
        }

        List<Integer> adminPermissionIntList = (List<Integer>) session.getAttribute(WebUtils.SESSION_CURRENT_PERMISSION);

        if (adminPermissionIntList == null
                || adminPermissionIntList.isEmpty()) {
            return false;
        }

        // 检查列表中是否包含目标权限 ID
        return adminPermissionIntList.contains(targetPermissionId);
    }

}