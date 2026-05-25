package com.campus.secondhand.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.secondhand.entity.Order;
import com.campus.secondhand.entity.Product;
import com.campus.secondhand.mapper.OrderMapper;
import com.campus.secondhand.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单服务实现类
 * 
 * 设计模式应用：
 * 1. 单例模式：Spring的@Service注解确保该类为单例
 * 2. 模板方法模式：继承ServiceImpl，复用基础CRUD模板
 * 3. 依赖倒置：实现IOrderService接口，供Controller依赖
 * 4. 状态模式：订单状态转换通过状态机管理（canTransitionStatus方法）
 */
@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    
    @Autowired
    private ProductMapper productMapper;
    
    // 状态转换规则定义
    private static final Map<String, String[]> STATE_TRANSITIONS = new HashMap<>();
    
    static {
        // 定义状态转换规则：当前状态 -> 允许转换的目标状态数组
        STATE_TRANSITIONS.put("pending", new String[]{"paid", "cancelled"});
        STATE_TRANSITIONS.put("paid", new String[]{"shipped", "cancelled"});
        STATE_TRANSITIONS.put("shipped", new String[]{"completed"});
        STATE_TRANSITIONS.put("completed", new String[]{"disputed"});
        STATE_TRANSITIONS.put("disputed", new String[]{"completed", "rejected"});
    }

    /**
     * 创建订单
     * @param order 订单信息
     * @return 是否创建成功
     */
    @Override
    public boolean createOrder(Order order) {
        // 检查商品库存
        Product product = productMapper.selectById(order.getProductId());
        if (product == null || product.getStock() < order.getQuantity()) {
            return false;
        }

        // 计算总价
        order.setTotalPrice(product.getPrice() * order.getQuantity());
        // 设置初始状态
        order.setOrderStatus("pending");
        order.setPaymentStatus("unpaid");

        // 保存订单
        boolean result = save(order);
        if (result) {
            // 减少商品库存
            product.setStock(product.getStock() - order.getQuantity());
            if (product.getStock() == 0) {
                product.setStatus("sold");
            }
            productMapper.updateById(product);
        }
        return result;
    }

    /**
     * 更新订单状态
     * @param orderId 订单ID
     * @param orderStatus 新状态
     * @return 是否更新成功
     */
    @Override
    public boolean updateOrderStatus(Integer orderId, String orderStatus) {
        Order order = getById(orderId);
        if (order == null) {
            return false;
        }
        
        // 状态模式：检查状态转换是否合法
        if (!canTransitionStatus(order.getOrderStatus(), orderStatus)) {
            return false;
        }
        
        order.setOrderId(orderId);
        order.setOrderStatus(orderStatus);
        return updateById(order);
    }

    /**
     * 更新支付状态
     * @param orderId 订单ID
     * @param paymentStatus 支付状态
     * @return 是否更新成功
     */
    @Override
    public boolean updatePaymentStatus(Integer orderId, String paymentStatus) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setPaymentStatus(paymentStatus);
        return updateById(order);
    }

    /**
     * 按买家查询订单
     * @param buyerId 买家ID
     * @return 订单列表
     */
    @Override
    public List<Order> getOrdersByBuyer(Integer buyerId) {
        return baseMapper.selectList(new LambdaQueryWrapper<Order>()
                .eq(Order::getBuyerId, buyerId)
                .orderByDesc(Order::getCreatedAt));
    }

    /**
     * 按卖家查询订单
     * @param sellerId 卖家ID
     * @return 订单列表
     */
    @Override
    public List<Order> getOrdersBySeller(Integer sellerId) {
        return baseMapper.selectList(new LambdaQueryWrapper<Order>()
                .eq(Order::getSellerId, sellerId)
                .orderByDesc(Order::getCreatedAt));
    }

    /**
     * 取消订单
     * @param orderId 订单ID
     * @return 是否取消成功
     */
    @Override
    public boolean cancelOrder(Integer orderId) {
        Order order = getById(orderId);
        if (order != null && canTransitionStatus(order.getOrderStatus(), "cancelled")) {
            // 更新订单状态
            order.setOrderStatus("cancelled");
            boolean result = updateById(order);
            if (result) {
                // 恢复商品库存
                Product product = productMapper.selectById(order.getProductId());
                if (product != null) {
                    product.setStock(product.getStock() + order.getQuantity());
                    if (product.getStatus().equals("sold")) {
                        product.setStatus("approved");
                    }
                    productMapper.updateById(product);
                }
            }
            return result;
        }
        return false;
    }
    
    /**
     * 检查订单状态是否可以转换
     * 状态模式核心方法：定义状态转换规则
     * @param currentStatus 当前状态
     * @param newStatus 新状态
     * @return 是否可以转换
     */
    @Override
    public boolean canTransitionStatus(String currentStatus, String newStatus) {
        String[] allowedTransitions = STATE_TRANSITIONS.get(currentStatus);
        if (allowedTransitions == null) {
            return false;
        }
        for (String allowed : allowedTransitions) {
            if (allowed.equals(newStatus)) {
                return true;
            }
        }
        return false;
    }
}
