package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import com.sky.service.ShoppingCartService;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author sharkCode
 * @date 2025/5/15 10:25
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private DishService dishService;

    /**
     * 添加商品到购物车
     * @param shoppingCartDTO
     */
    @Override
    @Transactional
    public void add(ShoppingCartDTO shoppingCartDTO) {
        // 查询数据库是否有该条记录
        List<ShoppingCart> record = shoppingCartMapper.query(shoppingCartDTO, BaseContext.getCurrentId());

        ShoppingCart shoppingCart;

        if(record == null || record.isEmpty()) {
            // 没有则增加一条记录
            shoppingCart = new ShoppingCart();
            // 复制属性
            BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);


            if (shoppingCart.getDishId() == null) {
                // 查询套餐表，获取套餐数据
                Setmeal setmeal = setmealService.query(shoppingCart.getSetmealId());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setCreateTime(LocalDateTime.now());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setUserId(BaseContext.getCurrentId());
                shoppingCart.setNumber(1);
                // 插入数据
            } else {
                // 查询菜单表，获取菜单数据
                Result<DishVO> dishVOResult = dishService.queryById(shoppingCart.getDishId());
                DishVO dish = dishVOResult.getData();
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setCreateTime(LocalDateTime.now());
                shoppingCart.setName(dish.getName());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setUserId(BaseContext.getCurrentId());
                shoppingCart.setNumber(1);
            }
            shoppingCartMapper.insert(shoppingCart);
        } else {
            // 有数量+1
            shoppingCart = record.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            shoppingCartMapper.update(shoppingCart);
        }
    }

    /**
     * 查看购物车接口
     * @return
     */
    @Override
    public List<ShoppingCart> list() {
        Long userId = BaseContext.getCurrentId();
        return shoppingCartMapper.list(userId);
    }

    /**
     * 移除购物车某条数据
     * @param shoppingCartDTO
     */
    @Override
    public void deleteByShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        // 查询数据库记录
        List<ShoppingCart> record = shoppingCartMapper.query(shoppingCartDTO, BaseContext.getCurrentId());
        ShoppingCart shoppingCart = record.get(0);
        Integer number = shoppingCart.getNumber();
        if(number != 1) {
            // 如果数量不为1,修改数量
            shoppingCart.setNumber(number - 1);
            shoppingCartMapper.update(shoppingCart);
        } else {
            // 删除
            shoppingCartMapper.remove(shoppingCartDTO, BaseContext.getCurrentId());
        }
    }

    /**
     * 清空购物车
     */
    @Override
    public void clean() {
        shoppingCartMapper.clean(BaseContext.getCurrentId());
    }
}
