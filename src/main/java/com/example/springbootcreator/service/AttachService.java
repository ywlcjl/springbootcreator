package com.example.springbootcreator.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.springbootcreator.entity.Attach;
import com.example.springbootcreator.mapper.AttachMapper;
import com.example.springbootcreator.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Attach
 */
@Service
public class AttachService {

    @Autowired
    private AttachMapper attachMapper;

    /**
     * 保存文章
     * @param saveAttach 待保存的文章实体
     * @return 保存后的文章实体
     */
    public Boolean save(Attach saveAttach) {
        LocalDateTime now = LocalDateTime.now();

        Boolean isNew = (saveAttach.getId() == null || saveAttach.getId() == 0) ? true : false;

        Integer affectedRow = 0;
        if (isNew) {
            // 新增操作
            affectedRow = attachMapper.insert(saveAttach);
        } else {
            // 更新
            saveAttach.setUpdateTime(now);
            affectedRow = attachMapper.updateById(saveAttach);
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
        if (id == null || id < 0) {
            return false;
        }
        Attach attach = attachMapper.selectById(id);

        if (attach == null || attach.getId() == null) {
            return false;
        }

        //删除数据库
        Integer affectedRow = attachMapper.deleteById(id);

        //物理目录 static/
        String fileStorageBasePath = CommonUtils.PATH_STATIC + "/";

        if (affectedRow > 0) {
            if (attach.getPath() != null) {
                //真实目录路径
                //Path uploadPath = Paths.get(attachDirPath);

                //删除物理图片
                Path originalFilePath = Paths.get(fileStorageBasePath, attach.getPath());
                File originalFile = originalFilePath.toFile();
                if (originalFile.exists()) {
                    originalFile.delete();
                }

                //删除缩略图
                String thumbPathString = CommonUtils.getImageSizePath(attach.getPath());
                Path thumbFilePath = Paths.get(fileStorageBasePath, thumbPathString);
                File thumbFile = thumbFilePath.toFile();
                if (thumbFile.exists()) {
                    thumbFile.delete();
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public void updateArticleId(Long articleId, List<Integer> attachIds) {
        if (articleId == null || articleId <= 0 || attachIds == null || attachIds.isEmpty()) {
            // 增加日志或异常处理
            System.err.println("Article ID 或 Attachment ID 列表为空，跳过更新。");
            return;
        }

        // 创建一个 Attach 实例，并设置要更新的值
        Attach updateAttach = new Attach();
        updateAttach.setArticleId(articleId); // 设置新的 article_id

        // 构建更新 Wrapper
        UpdateWrapper<Attach> updateWrapper = new UpdateWrapper<>();

        // 关键步骤：使用 .in() 方法构建 WHERE id IN (attachIds)
        updateWrapper.in("id", attachIds);

        // 调用 MyBatis-Plus 提供的 update(entity, wrapper) 方法
        int updatedCount = attachMapper.update(updateAttach, updateWrapper);

        System.out.println("成功更新了 " + updatedCount + " 条附件记录的 articleId");
    }

    public List<Attach> getAttachsByArticleId(Long articleId) {
        if (articleId == null || articleId <= 0) {
            return null;
        }
        QueryWrapper<Attach> attachQueryWrapper = new QueryWrapper<>();
        attachQueryWrapper.orderByAsc("id");
        attachQueryWrapper.eq("article_id", articleId);
        List<Attach> attachList = attachMapper.selectList(attachQueryWrapper);

        return attachList;
    }
}

