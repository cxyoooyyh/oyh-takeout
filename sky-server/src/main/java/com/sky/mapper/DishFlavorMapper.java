package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    void saveBatch(List<DishFlavor> flavors);

    @Delete("DELETE FROM dish_flavor WHERE dish_id = #{id}")
    void deleteByDishId(Long id);

    void deleteBatchByDishIds(List<Long> ids);

    @Select("SELECT * FROM dish_flavor WHERE  dish_id = #{dishId}")
    List<DishFlavor> getById(Long dishId);
}
