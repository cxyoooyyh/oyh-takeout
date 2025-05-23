package com.sky.mapper;

import com.sky.dto.DataOverViewQueryDTO;
import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user WHERE openid = #{openId}")
    User getByOpenId(String openId);

    void save(User user);

    @Select("SELECT * FROM user WHERE id = #{userId}")
    User getById(Long userId);

    int queryUserCount(DataOverViewQueryDTO queryDate);
}
