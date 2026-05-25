package com.campus.secondhand.service;

import com.campus.secondhand.entity.Review;
import com.campus.secondhand.service.strategy.SimpleAverageStrategy;
import com.campus.secondhand.service.strategy.WeightedAverageStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 信用评分策略单元测试
 * 
 * 测试用例：
 * 1. 空评价列表返回默认分数
 * 2. 简单平均策略计算
 * 3. 加权平均策略计算
 * 4. 策略模式切换
 */
public class CreditScoreStrategyTest {
    
    private SimpleAverageStrategy simpleStrategy;
    private WeightedAverageStrategy weightedStrategy;
    
    @BeforeEach
    void setUp() {
        simpleStrategy = new SimpleAverageStrategy();
        weightedStrategy = new WeightedAverageStrategy();
    }
    
    @Test
    void testSimpleStrategy_EmptyReviews() {
        List<Review> reviews = Collections.emptyList();
        
        double score = simpleStrategy.calculate(reviews);
        
        assertEquals(100.0, score);
    }
    
    @Test
    void testSimpleStrategy_NullReviews() {
        double score = simpleStrategy.calculate(null);
        
        assertEquals(100.0, score);
    }
    
    @Test
    void testSimpleStrategy_SingleReview() {
        Review review = new Review();
        review.setRating(5);
        
        double score = simpleStrategy.calculate(Arrays.asList(review));
        
        assertEquals(100.0, score); // 5 * 20 = 100
    }
    
    @Test
    void testSimpleStrategy_MultipleReviews() {
        Review review1 = new Review();
        review1.setRating(5);
        
        Review review2 = new Review();
        review2.setRating(4);
        
        double score = simpleStrategy.calculate(Arrays.asList(review1, review2));
        
        // 平均评分 = (5 + 4) / 2 = 4.5
        // 信用分数 = 4.5 * 20 = 90
        assertEquals(90.0, score);
    }
    
    @Test
    void testWeightedStrategy_EmptyReviews() {
        List<Review> reviews = Collections.emptyList();
        
        double score = weightedStrategy.calculate(reviews);
        
        assertEquals(100.0, score);
    }
    
    @Test
    void testWeightedStrategy_MultipleReviewsWithDifferentTimes() {
        // 近期评价
        Review recentReview = new Review();
        recentReview.setRating(5);
        recentReview.setCreatedAt(LocalDateTime.now());
        
        // 旧评价
        Review oldReview = new Review();
        oldReview.setRating(1);
        oldReview.setCreatedAt(LocalDateTime.now().minusMonths(6));
        
        double weightedScore = weightedStrategy.calculate(Arrays.asList(recentReview, oldReview));
        double simpleScore = simpleStrategy.calculate(Arrays.asList(recentReview, oldReview));
        
        // 加权策略应该更接近近期评价的分数
        // 因为近期评价权重更高，而近期评价是5分
        assertTrue(weightedScore > simpleScore);
    }
    
    @Test
    void testStrategyNames() {
        assertEquals("简单平均策略", simpleStrategy.getStrategyName());
        assertEquals("加权平均策略", weightedStrategy.getStrategyName());
    }
    
    @Test
    void testCreditScoreRange() {
        // 测试信用分数范围在0-100之间
        Review maxReview = new Review();
        maxReview.setRating(5);
        
        Review minReview = new Review();
        minReview.setRating(1);
        
        double maxScore = simpleStrategy.calculate(Arrays.asList(maxReview));
        double minScore = simpleStrategy.calculate(Arrays.asList(minReview));
        
        assertEquals(100.0, maxScore);
        assertEquals(20.0, minScore); // 1 * 20 = 20
    }
}
