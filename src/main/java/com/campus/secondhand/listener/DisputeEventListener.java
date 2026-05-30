package com.campus.secondhand.listener;

import com.campus.secondhand.entity.Dispute;
import com.campus.secondhand.entity.Order;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.event.DisputeEvent;
import com.campus.secondhand.mapper.OrderMapper;
import com.campus.secondhand.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 纠纷事件监听器
 *
 * 设计模式应用：观察者模式
 * 监听纠纷事件并触发相应的业务逻辑（信用分更新）
 */
@Component
public class DisputeEventListener {

    private final OrderMapper orderMapper;
    private final UserMapper userMapper;

    /**
     * 构造监听器
     * @param orderMapper 订单Mapper
     * @param userMapper 用户Mapper
     */
    @Autowired
    public DisputeEventListener(OrderMapper orderMapper, UserMapper userMapper) {
        this.orderMapper = orderMapper;
        this.userMapper = userMapper;
    }

    /**
     * 处理纠纷解决事件
     * @param event 纠纷事件
     */
    @EventListener
    public void handleDisputeResolved(DisputeEvent event) {
        String eventType = event.getEventType();
        Dispute dispute = event.getDispute();

        // 获取订单信息
        Order order = orderMapper.selectById(dispute.getOrderId());
        if (order == null) {
            return;
        }

        // 根据纠纷处理结果调整信用分
        if ("RESOLVED".equals(eventType)) {
            // 纠纷成立，申请人（买家）胜诉
            // 卖家信用分降低
            adjustCreditScore(order.getSellerId(), -10, "纠纷败诉");
            System.out.println("纠纷成立，卖家" + order.getSellerId() + "信用分-10");
        } else if ("REJECTED".equals(eventType)) {
            // 纠纷被驳回，卖家胜诉
            // 申请人（买家）信用分降低（恶意投诉）
            adjustCreditScore(dispute.getApplicantId(), -5, "恶意投诉");
            System.out.println("纠纷驳回，申请人" + dispute.getApplicantId() + "信用分-5");
        }
    }

    /**
     * 调整用户信用分
     * @param userId 用户ID
     * @param delta 变化量（正数为增加，负数为减少）
     * @param reason 原因
     */
    private void adjustCreditScore(Integer userId, int delta, String reason) {
        User user = userMapper.selectById(userId);
        if (user != null) {
            int newScore = Math.max(0, Math.min(100, user.getCreditScore() + delta));
            user.setCreditScore(newScore);
            userMapper.updateById(user);
        }
    }
}
