package com.campus.secondhand.service;

import com.campus.secondhand.entity.Review;
import com.campus.secondhand.mapper.ReviewMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewStorageTest {
    @Mock
    private ReviewMapper reviewMapper;
    @Mock
    private CreditScoreCalculator creditScoreCalculator;
    @InjectMocks
    private ReviewStorage reviewStorage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStoreReview_Success() {
        // 测试存储评价成功的情况
        Review review = new Review();
        review.setReviewedId(1);
        
        when(reviewMapper.insert(review)).thenReturn(1);
        
        boolean result = reviewStorage.storeReview(review);
        assertTrue(result);
        verify(creditScoreCalculator, times(1)).updateCreditScoreAfterReview(review);
    }

    @Test
    void testStoreReview_Failure() {
        // 测试存储评价失败的情况
        Review review = new Review();
        
        when(reviewMapper.insert(review)).thenReturn(0);
        
        boolean result = reviewStorage.storeReview(review);
        assertFalse(result);
        verify(creditScoreCalculator, never()).updateCreditScoreAfterReview(review);
    }

    @Test
    void testGetReviewsByReviewed() {
        // 测试根据被评价人ID获取评价列表
        List<Review> reviews = new ArrayList<>();
        Review review = new Review();
        reviews.add(review);
        
        when(reviewMapper.selectByReviewedId(1)).thenReturn(reviews);
        
        List<Review> result = reviewStorage.getReviewsByReviewed(1);
        assertEquals(reviews, result);
    }

    @Test
    void testGetReviewByOrder() {
        // 测试根据订单ID获取评价
        Review review = new Review();
        
        when(reviewMapper.selectByOrderId(1)).thenReturn(review);
        
        Review result = reviewStorage.getReviewByOrder(1);
        assertEquals(review, result);
    }

    @Test
    void testGetReviewById() {
        // 测试根据评价ID获取评价
        Review review = new Review();
        
        when(reviewMapper.selectById(1)).thenReturn(review);
        
        Review result = reviewStorage.getReviewById(1);
        assertEquals(review, result);
    }
}
