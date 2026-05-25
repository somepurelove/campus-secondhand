package com.campus.secondhand.service.strategy;

import com.campus.secondhand.entity.Review;
import java.util.List;

/**
 * 信用评分计算策略接口
 * 
 * 设计模式应用：策略模式
 * 定义信用评分计算的统一接口，支持多种计算策略
 */
public interface CreditScoreStrategy {
    
    /**
     * 计算信用评分
     * @param reviews 评价列表
     * @return 信用评分（0-100）
     */
    double calculate(List<Review> reviews);
    
    /**
     * 获取策略名称
     * @return 策略名称
     */
    String getStrategyName();
}
