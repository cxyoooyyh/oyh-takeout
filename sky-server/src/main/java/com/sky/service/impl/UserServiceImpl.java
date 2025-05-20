package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.DataOverViewQueryDTO;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sharkCode
 * @date 2025/5/8 13:02
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("appid", weChatProperties.getAppid());
        requestMap.put("secret", weChatProperties.getSecret());
        requestMap.put("js_code", userLoginDTO.getCode());
        requestMap.put("grant_type", "authorization_code");

        String response = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/jscode2session", requestMap);
        log.info("response: {}", response);
        JSONObject jsonObject = JSONObject.parseObject(response);
        String openId = jsonObject.getString("openid");
        // 登录失败
        if(openId == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        User user = userMapper.getByOpenId(openId);
        if (user == null) {
            // 创建新用户
            user = new User();
            user.setOpenid(openId);
            user.setCreateTime(LocalDateTime.now());
            userMapper.save(user);
        }
        return user;
    }

    // 通过日期查询用户数量
    @Override
    public int queryUserCount(DataOverViewQueryDTO queryDate) {
        return userMapper.queryUserCount(queryDate);
    }
}
