package com.campus.secondhand.service.strategy;

import com.campus.secondhand.entity.Review;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 简单平均策略
 * 
 * 设计模式应用：策略模式的具体实现
 * 直接计算所有评价的平均分，然后映射到0-100分
 */
@Component
public class SimpleAverageStrategy implements CreditScoreStrategy {
    
    @Override
    public double calculate(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 100.0; // 默认初始分数
        }
        
        // 计算平均评分
        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(5.0);
        
        // 评分范围：1-5分，信用分数范围：0-100分
        double creditScore = averageRating * 20;
        
        // 确保信用分数在合理范围内
        return Math.max(0, Math.min(100, creditScore));
    }
    
    @Override
    public String getStrategyName() {
        return "简单平均策略";
    }
}
