package com.example.springbootcreator.service;

import com.example.springbootcreator.entity.AdminPermission;
import com.example.springbootcreator.mapper.AdminPermissionMapper;
import com.example.springbootcreator.util.WebUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * 文章分类业务逻辑服务。
 */
@Service
public class AdminPermissionService {

    @Autowired
    private AdminPermissionMapper adminPermissionMapper;

    /**
     * 新增或更新
     * @param saveAdminPermission 待保存的分类实体
     * @return Boolean
     */
    public Boolean save(AdminPermission saveAdminPermission) {
        LocalDateTime now = LocalDateTime.now();

        Boolean isNew = (saveAdminPermission.getId() == null || saveAdminPermission.getId() == 0) ? true : false;
        Integer affectedRow = 0;

        if (isNew) {
            // 新增操作
            affectedRow = adminPermissionMapper.insert(saveAdminPermission);
        } else {
            // 更新
            saveAdminPermission.setUpdateTime(now);
            affectedRow = adminPermissionMapper.updateById(saveAdminPermission);
        }

        if (affectedRow > 0) {
            return true;
        } else  {
            return false;
        }
    }



}
