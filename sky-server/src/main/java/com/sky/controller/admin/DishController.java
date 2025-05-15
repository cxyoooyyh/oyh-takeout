package com.sky.controller.admin;

import com.sky.constant.RedisFieldConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author sharkCode
 * @date 2025/5/6 17:10
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    @ApiOperation("新增菜品接口")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品 {}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        deleteRedisDishKey(RedisFieldConstant.DISH_CATEGORY_KEY + ":" + dishDTO.getCategoryId());
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("菜品分页查询接口")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询 {}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }
    @DeleteMapping
    @ApiOperation("菜品批量删除接口")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("菜品批量删除 {}", ids);
        dishService.deleteBatch(ids);
        deleteRedisDishKey(RedisFieldConstant.DISH_CATEGORY_KEY + "*");
        return Result.success();
    }
    @GetMapping("/{id}")
    @ApiOperation("菜品详情查询接口")
    public Result<DishVO> queryById(@PathVariable(value = "id") Long id) {
        return dishService.queryById(id);
    }

    @PutMapping
    @ApiOperation("菜品修改接口")
    public Result update(@RequestBody DishDTO dishDTO) {
        dishService.update(dishDTO);
        deleteRedisDishKey(RedisFieldConstant.DISH_CATEGORY_KEY + "*");
        return Result.success();
    }

    /**
     * 删除菜品缓存
     */
    private void deleteRedisDishKey(String Pattern) {
        Set dishKeys = redisTemplate.keys(Pattern);
        redisTemplate.delete(dishKeys);
    }
}
