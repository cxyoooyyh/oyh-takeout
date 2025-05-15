package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sharkCode
 * @date 2025/5/15 10:22
 */
@RestController
@RequestMapping("/user/shoppingCart/")
@Api("购物车相关接口")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加商品到购物车
     */
    @PostMapping("/add")
    @ApiOperation("添加商品到购物车")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }
    /**
     * 查询购物车列表
     */
    @GetMapping("/list")
    @ApiOperation("查看购物车接口")
    public Result<List<ShoppingCart>> list() {
        return Result.success(shoppingCartService.list());
    }
    /**
     * 根据 dishId和dishFlavor 或 set_meal 删除购物车中数据
     */
    @PostMapping("/sub")
    @ApiOperation("删除购物车某个数据")
    public Result deleteById(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.deleteByShoppingCart(shoppingCartDTO);
        return Result.success();
    }
    /**
     * 清空购物车
     */
    @DeleteMapping("/clean")
    public Result cleanShoppingCart() {
        shoppingCartService.clean();
        return Result.success();
    }
}
