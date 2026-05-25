package com.campus.secondhand.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.secondhand.entity.Review;
import com.campus.secondhand.event.ReviewEvent;
import com.campus.secondhand.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评价服务实现类
 * 
 * 设计模式应用：
 * 1. 单例模式：Spring的@Service注解确保该类为单例
 * 2. 模板方法模式：继承ServiceImpl，复用基础CRUD模板
 * 3. 依赖倒置：实现IReviewService接口，供Controller依赖
 * 4. 观察者模式：通过ApplicationEventPublisher发布评价事件，触发信用评分更新
 */
@Service
public class ReviewService extends ServiceImpl<ReviewMapper, Review> implements IReviewService {
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    /**
     * 添加评价
     * @param review 评价信息
     * @return 是否添加成功
     */
    @Override
    public boolean addReview(Review review) {
        boolean result = save(review);
        if (result) {
            // 观察者模式：发布评价创建事件，通知其他组件
            eventPublisher.publishEvent(new ReviewEvent(this, review, "CREATED"));
        }
        return result;
    }

    /**
     * 按被评价人查询评价
     * @param reviewedId 被评价人ID
     * @return 评价列表
     */
    @Override
    public List<Review> getReviewsByReviewed(Integer reviewedId) {
        return baseMapper.selectList(new LambdaQueryWrapper<Review>()
                .eq(Review::getReviewedId, reviewedId)
                .orderByDesc(Review::getCreatedAt));
    }

    /**
     * 按订单查询评价
     * @param orderId 订单ID
     * @return 评价信息
     */
    @Override
    public Review getReviewByOrder(Integer orderId) {
        return baseMapper.selectOne(new LambdaQueryWrapper<Review>()
                .eq(Review::getOrderId, orderId));
    }

    /**
     * 计算用户平均评分
     * @param userId 用户ID
     * @return 平均评分
     */
    @Override
    public Double getAverageRating(Integer userId) {
        List<Review> reviews = baseMapper.selectList(new LambdaQueryWrapper<Review>()
                .eq(Review::getReviewedId, userId));
        if (reviews.isEmpty()) {
            return 5.0; // 默认5分
        }
        int totalRating = 0;
        for (Review review : reviews) {
            totalRating += review.getRating();
        }
        return (double) totalRating / reviews.size();
    }
}
