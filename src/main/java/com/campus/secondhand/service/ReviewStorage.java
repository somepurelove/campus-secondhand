package com.campus.secondhand.service;

import com.campus.secondhand.entity.Review;
import com.campus.secondhand.mapper.ReviewMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评价记录存储类
 * 负责评价的存储和管理
 */
@Service
public class ReviewStorage {
    private final ReviewMapper reviewMapper;
    private final CreditScoreCalculator creditScoreCalculator;

    public ReviewStorage(ReviewMapper reviewMapper, CreditScoreCalculator creditScoreCalculator) {
        this.reviewMapper = reviewMapper;
        this.creditScoreCalculator = creditScoreCalculator;
    }

    /**
     * 存储评价记录
     * @param review 评价对象
     * @return 是否存储成功
     */
    public boolean storeReview(Review review) {
        // 存储评价
        int result = reviewMapper.insert(review);
        
        if (result > 0) {
            // 存储成功后更新被评价人的信用评分
            creditScoreCalculator.updateCreditScoreAfterReview(review);
            return true;
        }
        return false;
    }

    /**
     * 根据被评价人ID获取评价列表
     * @param reviewedId 被评价人ID
     * @return 评价列表
     */
    public List<Review> getReviewsByReviewed(Integer reviewedId) {
        return reviewMapper.selectByReviewedId(reviewedId);
    }

    /**
     * 根据订单ID获取评价
     * @param orderId 订单ID
     * @return 评价对象
     */
    public Review getReviewByOrder(Integer orderId) {
        return reviewMapper.selectByOrderId(orderId);
    }

    /**
     * 根据评价ID获取评价
     * @param reviewId 评价ID
     * @return 评价对象
     */
    public Review getReviewById(Integer reviewId) {
        return reviewMapper.selectById(reviewId);
    }
}
