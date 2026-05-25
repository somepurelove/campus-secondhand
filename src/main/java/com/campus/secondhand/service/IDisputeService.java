package com.campus.secondhand.service;

import com.campus.secondhand.entity.Dispute;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 纠纷服务接口
 * 
 * 设计模式应用：
 * 1. 接口隔离原则：定义精简的纠纷相关操作接口
 * 2. 依赖倒置：Controller依赖此接口而非具体实现
 */
public interface IDisputeService extends IService<Dispute> {
    
    /**
     * 提交纠纷
     * @param dispute 纠纷信息
     * @return 是否提交成功
     */
    boolean submitDispute(Dispute dispute);
    
    /**
     * 处理纠纷
     * @param disputeId 纠纷ID
     * @param auditorId 审核员ID
     * @param status 处理状态
     * @param resolution 解决方案
     * @return 是否处理成功
     */
    boolean handleDispute(Integer disputeId, Integer auditorId, String status, String resolution);
    
    /**
     * 根据申请人查询纠纷
     * @param applicantId 申请人ID
     * @return 纠纷列表
     */
    List<Dispute> getDisputesByApplicant(Integer applicantId);
    
    /**
     * 根据状态查询纠纷
     * @param status 纠纷状态
     * @return 纠纷列表
     */
    List<Dispute> getDisputesByStatus(String status);
    
    /**
     * 根据订单查询纠纷
     * @param orderId 订单ID
     * @return 纠纷信息
     */
    Dispute getDisputeByOrder(Integer orderId);
}
