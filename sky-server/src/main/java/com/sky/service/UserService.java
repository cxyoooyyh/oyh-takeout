package com.sky.service;

import com.sky.dto.DataOverViewQueryDTO;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @author sharkCode
 * @date 2025/5/8 11:39
 */
public interface UserService {
    User wxLogin(UserLoginDTO userLoginDTO);

    int queryUserCount(DataOverViewQueryDTO queryDate);
}
