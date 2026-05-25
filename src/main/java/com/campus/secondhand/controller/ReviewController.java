package com.campus.secondhand.controller;

import com.campus.secondhand.entity.Review;
import com.campus.secondhand.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评价控制器
 * 
 * 设计模式应用：
 * 1. 依赖倒置：依赖IReviewService接口而非具体实现
 * 2. MVC模式：作为Controller层处理HTTP请求
 * 3. 观察者模式：通过IReviewService.addReview触发信用评分更新
 */
@RestController
@RequestMapping("/review")
public class ReviewController {
    
    private final IReviewService reviewService;
    
    /**
     * 构造控制器
     * @param reviewService 评价服务接口
     */
    @Autowired
    public ReviewController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * 添加评价
     * @param review 评价信息
     * @return 添加结果
     */
    @PostMapping("/add")
    public Map<String, Object> addReview(@RequestBody Review review) {
        Map<String, Object> result = new HashMap<>();
        boolean success = reviewService.addReview(review);
        if (success) {
            result.put("code", 200);
            result.put("message", "评价成功");
        } else {
            result.put("code", 500);
            result.put("message", "评价失败");
        }
        return result;
    }

    /**
     * 按被评价人查询评价
     * @param reviewedId 被评价人ID
     * @return 评价列表
     */
    @GetMapping("/reviewed/{reviewedId}")
    public Map<String, Object> getReviewsByReviewed(@PathVariable Integer reviewedId) {
        Map<String, Object> result = new HashMap<>();
        List<Review> reviews = reviewService.getReviewsByReviewed(reviewedId);
        result.put("code", 200);
        result.put("reviews", reviews);
        return result;
    }

    /**
     * 按订单查询评价
     * @param orderId 订单ID
     * @return 评价信息
     */
    @GetMapping("/order/{orderId}")
    public Map<String, Object> getReviewByOrder(@PathVariable Integer orderId) {
        Map<String, Object> result = new HashMap<>();
        Review review = reviewService.getReviewByOrder(orderId);
        if (review != null) {
            result.put("code", 200);
            result.put("review", review);
        } else {
            result.put("code", 404);
            result.put("message", "评价不存在");
        }
        return result;
    }

    /**
     * 获取用户平均评分
     * @param userId 用户ID
     * @return 平均评分
     */
    @GetMapping("/rating/{userId}")
    public Map<String, Object> getAverageRating(@PathVariable Integer userId) {
        Map<String, Object> result = new HashMap<>();
        Double rating = reviewService.getAverageRating(userId);
        result.put("code", 200);
        result.put("rating", rating);
        return result;
    }
}
