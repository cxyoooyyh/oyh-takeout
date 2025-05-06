package com.sky.service.impl;

import com.sky.service.CommonService;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author sharkCode
 * @date 2025/5/6 16:01
 */
@Service
@Slf4j
public class CommonServiceImpl implements CommonService {
    @Autowired
    private AliOssUtil aliOssUtil;
    @Override
    public String upload(MultipartFile file) {
        String filePath = "";
        try {
            String originalFilename = file.getOriginalFilename();
            // 获取文件扩展名
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            String newFileName = UUID.randomUUID().toString() + extension;

            filePath = aliOssUtil.upload(file.getBytes(), newFileName);

        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
        }
        return filePath;
    }
}
