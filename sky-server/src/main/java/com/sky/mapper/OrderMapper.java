package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author sharkCode
 * @date 2025/5/15 19:54
 */
@Mapper
public interface OrderMapper {
    void insert(Orders orders);
    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);
    // 超时 15 分钟 未付款
    @Select("SELECT * FROM orders WHERE status = #{pendingPayment} and delivery_time < #{overTime}")
    List<Orders> queryByOrderTimeout(Integer pendingPayment, LocalDateTime overTime);

    List<GoodsSalesDTO> queryTop10(LocalDateTime begin, LocalDateTime end);
}
