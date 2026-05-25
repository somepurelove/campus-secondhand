package com.campus.secondhand.service;

import com.campus.secondhand.entity.Review;
import com.campus.secondhand.service.strategy.CreditScoreStrategy;
import com.campus.secondhand.service.strategy.SimpleAverageStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 信用评分计算器
 * 
 * 设计模式应用：
 * 1. 单例模式：Spring的@Service注解确保该类为单例
 * 2. 策略模式：通过CreditScoreStrategy接口支持多种评分计算策略
 *    - SimpleAverageStrategy：简单平均策略
 *    - WeightedAverageStrategy：加权平均策略（考虑时间权重）
 * 3. 依赖倒置：依赖IReviewService和IUserService接口
 */
@Service
public class CreditScoreCalculator {
    
    private final IReviewService reviewService;
    private final IUserService userService;
    private CreditScoreStrategy strategy;
    
    /**
     * 构造计算器
     * @param reviewService 评价服务接口
     * @param userService 用户服务接口
     * @param defaultStrategy 默认策略（简单平均策略）
     */
    @Autowired
    public CreditScoreCalculator(IReviewService reviewService, IUserService userService, 
                                  SimpleAverageStrategy defaultStrategy) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.strategy = defaultStrategy;
    }
    
    /**
     * 设置评分计算策略
     * @param strategy 评分策略
     */
    public void setStrategy(CreditScoreStrategy strategy) {
        this.strategy = strategy;
    }
    
    /**
     * 获取当前策略名称
     * @return 策略名称
     */
    public String getCurrentStrategyName() {
        return strategy.getStrategyName();
    }

    /**
     * 计算用户的信用评分
     * @param userId 用户ID
     * @return 信用评分
     */
    public double calculateCreditScore(Integer userId) {
        // 获取用户的所有评价
        List<Review> reviews = reviewService.getReviewsByReviewed(userId);
        
        // 使用策略模式计算信用评分
        return strategy.calculate(reviews);
    }

    /**
     * 更新用户的信用评分
     * @param userId 用户ID
     * @return 更新后的信用分数
     */
    public double updateCreditScore(Integer userId) {
        double creditScore = calculateCreditScore(userId);
        userService.updateCreditScore(userId, (int) Math.round(creditScore));
        return creditScore;
    }

    /**
     * 在添加新评价后更新相关用户的信用评分
     * @param review 新评价
     */
    public void updateCreditScoreAfterReview(Review review) {
        // 更新被评价人的信用评分
        updateCreditScore(review.getReviewedId());
    }
}
