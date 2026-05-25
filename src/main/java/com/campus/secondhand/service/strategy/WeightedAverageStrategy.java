package com.campus.secondhand.service.strategy;

import com.campus.secondhand.entity.Review;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 加权平均策略
 * 
 * 设计模式应用：策略模式的具体实现
 * 近期评价的权重更高，时间越近权重越高
 */
@Component
public class WeightedAverageStrategy implements CreditScoreStrategy {
    
    @Override
    public double calculate(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 100.0; // 默认初始分数
        }
        
        LocalDateTime now = LocalDateTime.now();
        double weightedSum = 0;
        double weightSum = 0;
        
        for (Review review : reviews) {
            // 计算时间权重：越近期的评价权重越高
            long daysAgo = ChronoUnit.DAYS.between(review.getCreatedAt(), now);
            double weight = Math.max(0.1, 1.0 - (daysAgo / 365.0)); // 一年前权重最低为0.1
            
            weightedSum += review.getRating() * weight;
            weightSum += weight;
        }
        
        double averageRating = weightedSum / weightSum;
        double creditScore = averageRating * 20;
        
        return Math.max(0, Math.min(100, creditScore));
    }
    
    @Override
    public String getStrategyName() {
        return "加权平均策略";
    }
}
