package com.sky.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author sharkCode
 * @date 2025/5/6 16:01
 */
public interface CommonService {
    String upload(MultipartFile file);
}
