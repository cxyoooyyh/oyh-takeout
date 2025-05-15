package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    List<ShoppingCart> query(@Param("dto") ShoppingCartDTO shoppingCartDTO,
                             @Param("userId") Long userId);

    /**
     * 插入一条购物车记录
     * @param shoppingCart
     */
    void insert(ShoppingCart shoppingCart);

    /**
     * 更新购物车记录
     * @param shoppingCart
     */
    void update(ShoppingCart shoppingCart);

    List<ShoppingCart> list(Long userId);

    void remove(@Param("dto") ShoppingCartDTO shoppingCartDTO,@Param("userId") Long userId);

    void clean(Long userId);
}
