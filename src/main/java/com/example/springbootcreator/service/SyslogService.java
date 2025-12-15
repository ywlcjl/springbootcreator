package com.example.springbootcreator.service;

import com.example.springbootcreator.entity.Syslog;
import com.example.springbootcreator.entity.Admin;
import com.example.springbootcreator.mapper.SyslogMapper;
import com.example.springbootcreator.util.WebUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Syslog
 */
@Service
public class SyslogService {

    @Autowired
    private SyslogMapper syslogMapper;

    @Autowired
    private AuthService authService;

    /**
     * 保存文章
     * @param saveSyslog 待保存的文章实体
     * @return 保存后的文章实体
     */
    public Boolean save(Syslog saveSyslog) {
        LocalDateTime now = LocalDateTime.now();

        Boolean isNew = (saveSyslog.getId() == null || saveSyslog.getId() == 0) ? true : false;

        Integer affectedRow = 0;

        if (isNew) {
            // 新增操作
            affectedRow = syslogMapper.insert(saveSyslog);
        } else {
            // 更新
            saveSyslog.setUpdateTime(now);
            affectedRow = syslogMapper.updateById(saveSyslog);
        }

        if (affectedRow > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据 ID 删除文章
     * @param id 文章ID
     */
    public Boolean deleteById(Long id) {
        Integer affectedRow = syslogMapper.deleteById(id);

        if (affectedRow > 0) {
            return true;
        } else {
            return  false;
        }
    }


    /**
     * type类型对应名称
     */
    private static final Map<Integer, String> TYPE_NAME_MAP;
    static {
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "默认类型");
        map.put(1, "登录系统");
        map.put(2, "删除文章");

        TYPE_NAME_MAP = Collections.unmodifiableMap(map);
    }
    public String getTypeName(Integer type) {
        return TYPE_NAME_MAP.getOrDefault(type, "未知类型");
    }
    public Map<Integer, String> getSyslogTypeMap() {
        return TYPE_NAME_MAP;
    }

    /**
     * 添加日志
     * @param type
     * @param content
     * @return
     */
    public boolean addSyslog(Integer type, String content) {
        LocalDateTime now = LocalDateTime.now();

        Syslog saveSyslog = new Syslog();

        String typeName = TYPE_NAME_MAP.get(type);

        saveSyslog.setType(type);
        saveSyslog.setTypeName(typeName);
        saveSyslog.setContent(content);
        saveSyslog.setUpdateTime(now);

        HttpSession session = WebUtils.getCurrentSession();
        Admin admin = (Admin) session.getAttribute(WebUtils.SESSION_CURRENT_ADMIN);
        saveSyslog.setAdminId(admin.getId());
        saveSyslog.setUsername(admin.getUsername());

        String ipAddress = WebUtils.getClientIp();
        saveSyslog.setIpAddress(ipAddress);
        Integer affectedRow = syslogMapper.insert(saveSyslog);
        if (affectedRow > 0){
            return true;
        } else {
            return false;
        }
    }
}

