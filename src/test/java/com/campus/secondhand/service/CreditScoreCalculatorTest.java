package com.campus.secondhand.service;

import com.campus.secondhand.entity.Review;
import com.campus.secondhand.service.strategy.CreditScoreStrategy;
import com.campus.secondhand.service.strategy.SimpleAverageStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CreditScoreCalculatorTest {
    @Mock
    private IReviewService reviewService;
    @Mock
    private IUserService userService;
    @Mock
    private SimpleAverageStrategy strategy;
    @InjectMocks
    private CreditScoreCalculator creditScoreCalculator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // 设置策略的默认行为
        when(strategy.getStrategyName()).thenReturn("简单平均策略");
    }

    @Test
    void testCalculateCreditScore_EmptyReviews() {
        // 测试没有评价的情况
        List<Review> emptyList = new ArrayList<>();
        when(reviewService.getReviewsByReviewed(1)).thenReturn(emptyList);
        when(strategy.calculate(emptyList)).thenReturn(100.0);
        
        double score = creditScoreCalculator.calculateCreditScore(1);
        assertEquals(100.0, score);
    }

    @Test
    void testCalculateCreditScore_WithReviews() {
        // 测试有评价的情况
        List<Review> reviews = new ArrayList<>();
        Review review1 = new Review();
        review1.setRating(5);
        Review review2 = new Review();
        review2.setRating(4);
        reviews.add(review1);
        reviews.add(review2);
        
        when(reviewService.getReviewsByReviewed(1)).thenReturn(reviews);
        when(strategy.calculate(reviews)).thenReturn(90.0);
        
        double score = creditScoreCalculator.calculateCreditScore(1);
        assertEquals(90.0, score);
    }

    @Test
    void testUpdateCreditScore() {
        // 测试更新信用评分
        List<Review> emptyList = new ArrayList<>();
        when(reviewService.getReviewsByReviewed(1)).thenReturn(emptyList);
        when(strategy.calculate(emptyList)).thenReturn(100.0);
        when(userService.updateCreditScore(1, 100)).thenReturn(true);
        
        double score = creditScoreCalculator.updateCreditScore(1);
        assertEquals(100.0, score);
        verify(userService, times(1)).updateCreditScore(1, 100);
    }

    @Test
    void testUpdateCreditScoreAfterReview() {
        // 测试添加评价后更新信用评分
        Review review = new Review();
        review.setReviewedId(1);
        
        List<Review> emptyList = new ArrayList<>();
        when(reviewService.getReviewsByReviewed(1)).thenReturn(emptyList);
        when(strategy.calculate(emptyList)).thenReturn(100.0);
        when(userService.updateCreditScore(1, 100)).thenReturn(true);
        
        creditScoreCalculator.updateCreditScoreAfterReview(review);
        verify(userService, times(1)).updateCreditScore(1, 100);
    }
}
