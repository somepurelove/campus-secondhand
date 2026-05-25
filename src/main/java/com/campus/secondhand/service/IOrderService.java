package com.campus.secondhand.service;

import com.campus.secondhand.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 订单服务接口
 * 
 * 设计模式应用：
 * 1. 接口隔离原则：定义精简的订单相关操作接口
 * 2. 依赖倒置：Controller依赖此接口而非具体实现
 * 3. 状态模式：订单状态转换通过状态机管理（具体实现见OrderState接口）
 */
public interface IOrderService extends IService<Order> {
    
    /**
     * 创建订单
     * @param order 订单信息
     * @return 是否创建成功
     */
    boolean createOrder(Order order);
    
    /**
     * 更新订单状态
     * @param orderId 订单ID
     * @param orderStatus 新状态
     * @return 是否更新成功
     */
    boolean updateOrderStatus(Integer orderId, String orderStatus);
    
    /**
     * 更新支付状态
     * @param orderId 订单ID
     * @param paymentStatus 支付状态
     * @return 是否更新成功
     */
    boolean updatePaymentStatus(Integer orderId, String paymentStatus);
    
    /**
     * 根据买家查询订单
     * @param buyerId 买家ID
     * @return 订单列表
     */
    List<Order> getOrdersByBuyer(Integer buyerId);
    
    /**
     * 根据卖家查询订单
     * @param sellerId 卖家ID
     * @return 订单列表
     */
    List<Order> getOrdersBySeller(Integer sellerId);
    
    /**
     * 取消订单
     * @param orderId 订单ID
     * @return 是否取消成功
     */
    boolean cancelOrder(Integer orderId);
    
    /**
     * 检查订单状态是否可以转换
     * @param currentStatus 当前状态
     * @param newStatus 新状态
     * @return 是否可以转换
     */
    boolean canTransitionStatus(String currentStatus, String newStatus);
}
