package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author sharkCode
 * @date 2025/5/7 16:20
 */
@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺相关操作接口")
@Slf4j
public class ShopController {
    public static final String SHOP_STATUS_KEY = "shop_status";

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/status")
    @ApiOperation("获取店铺状态接口")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(SHOP_STATUS_KEY);
        return Result.success(status);
    }
}

