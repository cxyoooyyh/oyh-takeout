package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author sharkCode
 * @date 2025/5/16 11:40
 */
@Component
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;
    /**
     * 订单超时未付款，自动取消
     */
    @Scheduled(cron = "0 */1 * * * *")
    public void OrderTimeoutAutoClose() {
        LocalDateTime overTime = LocalDateTime.now().plusMinutes(-15);
        List<Orders> ordersList = orderMapper.queryByOrderTimeout(
                Orders.PENDING_PAYMENT, overTime

        );
        for (Orders orders : ordersList) {
            orders.setStatus(Orders.CANCELLED);
            orders.setCancelReason("订单超时，系统自动取消");
            orders.setCancelTime(LocalDateTime.now());
            orderMapper.update(orders);
        }
    }
}
