package com.campus.secondhand.controller;

import com.campus.secondhand.entity.Order;
import com.campus.secondhand.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单控制器
 * 
 * 设计模式应用：
 * 1. 依赖倒置：依赖IOrderService接口而非具体实现
 * 2. MVC模式：作为Controller层处理HTTP请求
 * 3. 状态模式：通过IOrderService的canTransitionStatus方法实现状态机
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    
    private final IOrderService orderService;
    
    /**
     * 构造控制器
     * @param orderService 订单服务接口
     */
    @Autowired
    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 创建订单
     * @param order 订单信息
     * @return 创建结果
     */
    @PostMapping("/create")
    public Map<String, Object> createOrder(@RequestBody Order order) {
        Map<String, Object> result = new HashMap<>();
        boolean success = orderService.createOrder(order);
        if (success) {
            result.put("code", 200);
            result.put("message", "订单创建成功");
        } else {
            result.put("code", 500);
            result.put("message", "订单创建失败，商品库存不足");
        }
        return result;
    }

    /**
     * 更新订单状态
     * @param statusInfo 状态信息
     * @return 更新结果
     */
    @PutMapping("/status")
    public Map<String, Object> updateOrderStatus(@RequestBody Map<String, Object> statusInfo) {
        Map<String, Object> result = new HashMap<>();
        Integer orderId = (Integer) statusInfo.get("orderId");
        String orderStatus = (String) statusInfo.get("orderStatus");
        
        // 检查状态转换是否合法
        Order order = orderService.getById(orderId);
        if (order != null && !orderService.canTransitionStatus(order.getOrderStatus(), orderStatus)) {
            result.put("code", 400);
            result.put("message", "订单状态转换不合法");
            return result;
        }
        
        boolean success = orderService.updateOrderStatus(orderId, orderStatus);
        if (success) {
            result.put("code", 200);
            result.put("message", "订单状态更新成功");
        } else {
            result.put("code", 500);
            result.put("message", "订单状态更新失败");
        }
        return result;
    }

    /**
     * 更新支付状态
     * @param paymentInfo 支付信息
     * @return 更新结果
     */
    @PutMapping("/payment")
    public Map<String, Object> updatePaymentStatus(@RequestBody Map<String, Object> paymentInfo) {
        Map<String, Object> result = new HashMap<>();
        Integer orderId = (Integer) paymentInfo.get("orderId");
        String paymentStatus = (String) paymentInfo.get("paymentStatus");
        boolean success = orderService.updatePaymentStatus(orderId, paymentStatus);
        if (success) {
            result.put("code", 200);
            result.put("message", "支付状态更新成功");
        } else {
            result.put("code", 500);
            result.put("message", "支付状态更新失败");
        }
        return result;
    }

    /**
     * 获取订单详情
     * @param orderId 订单ID
     * @return 订单详情
     */
    @GetMapping("/detail/{orderId}")
    public Map<String, Object> getOrderDetail(@PathVariable Integer orderId) {
        Map<String, Object> result = new HashMap<>();
        Order order = orderService.getById(orderId);
        if (order != null) {
            result.put("code", 200);
            result.put("order", order);
        } else {
            result.put("code", 404);
            result.put("message", "订单不存在");
        }
        return result;
    }

    /**
     * 按买家查询订单
     * @param buyerId 买家ID
     * @return 订单列表
     */
    @GetMapping("/buyer/{buyerId}")
    public Map<String, Object> getOrdersByBuyer(@PathVariable Integer buyerId) {
        Map<String, Object> result = new HashMap<>();
        List<Order> orders = orderService.getOrdersByBuyer(buyerId);
        result.put("code", 200);
        result.put("orders", orders);
        return result;
    }

    /**
     * 按卖家查询订单
     * @param sellerId 卖家ID
     * @return 订单列表
     */
    @GetMapping("/seller/{sellerId}")
    public Map<String, Object> getOrdersBySeller(@PathVariable Integer sellerId) {
        Map<String, Object> result = new HashMap<>();
        List<Order> orders = orderService.getOrdersBySeller(sellerId);
        result.put("code", 200);
        result.put("orders", orders);
        return result;
    }

    /**
     * 取消订单
     * @param orderId 订单ID
     * @return 取消结果
     */
    @PutMapping("/cancel/{orderId}")
    public Map<String, Object> cancelOrder(@PathVariable Integer orderId) {
        Map<String, Object> result = new HashMap<>();
        boolean success = orderService.cancelOrder(orderId);
        if (success) {
            result.put("code", 200);
            result.put("message", "订单取消成功");
        } else {
            result.put("code", 500);
            result.put("message", "订单取消失败，订单状态不允许取消");
        }
        return result;
    }
}
