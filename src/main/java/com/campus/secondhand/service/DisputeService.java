package com.campus.secondhand.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.secondhand.entity.Dispute;
import com.campus.secondhand.entity.Order;
import com.campus.secondhand.mapper.DisputeMapper;
import com.campus.secondhand.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 纠纷服务实现类
 * 
 * 设计模式应用：
 * 1. 单例模式：Spring的@Service注解确保该类为单例
 * 2. 模板方法模式：继承ServiceImpl，复用基础CRUD模板
 * 3. 依赖倒置：实现IDisputeService接口，供Controller依赖
 */
@Service
public class DisputeService extends ServiceImpl<DisputeMapper, Dispute> implements IDisputeService {
    
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 提交纠纷
     * @param dispute 纠纷信息
     * @return 是否提交成功
     */
    @Override
    public boolean submitDispute(Dispute dispute) {
        // 设置初始状态
        dispute.setStatus("pending");
        boolean result = save(dispute);
        if (result) {
            // 更新订单状态为纠纷中
            Order order = orderMapper.selectById(dispute.getOrderId());
            if (order != null) {
                order.setOrderStatus("disputed");
                orderMapper.updateById(order);
            }
        }
        return result;
    }

    /**
     * 处理纠纷
     * @param disputeId 纠纷ID
     * @param auditorId 审核员ID
     * @param status 处理状态
     * @param resolution 解决方案
     * @return 是否处理成功
     */
    @Override
    public boolean handleDispute(Integer disputeId, Integer auditorId, String status, String resolution) {
        Dispute dispute = getById(disputeId);
        if (dispute != null) {
            dispute.setStatus(status);
            dispute.setAuditorId(auditorId);
            dispute.setResolution(resolution);
            boolean result = updateById(dispute);
            if (result) {
                // 更新订单状态
                Order order = orderMapper.selectById(dispute.getOrderId());
                if (order != null) {
                    if (status.equals("resolved") || status.equals("rejected")) {
                        order.setOrderStatus("completed");
                    }
                    orderMapper.updateById(order);
                }
            }
            return result;
        }
        return false;
    }

    /**
     * 按申请人查询纠纷
     * @param applicantId 申请人ID
     * @return 纠纷列表
     */
    @Override
    public List<Dispute> getDisputesByApplicant(Integer applicantId) {
        return baseMapper.selectList(new LambdaQueryWrapper<Dispute>()
                .eq(Dispute::getApplicantId, applicantId)
                .orderByDesc(Dispute::getCreatedAt));
    }

    /**
     * 按状态查询纠纷
     * @param status 纠纷状态
     * @return 纠纷列表
     */
    @Override
    public List<Dispute> getDisputesByStatus(String status) {
        return baseMapper.selectList(new LambdaQueryWrapper<Dispute>()
                .eq(Dispute::getStatus, status)
                .orderByDesc(Dispute::getCreatedAt));
    }

    /**
     * 按订单查询纠纷
     * @param orderId 订单ID
     * @return 纠纷信息
     */
    @Override
    public Dispute getDisputeByOrder(Integer orderId) {
        return baseMapper.selectOne(new LambdaQueryWrapper<Dispute>()
                .eq(Dispute::getOrderId, orderId));
    }
}
