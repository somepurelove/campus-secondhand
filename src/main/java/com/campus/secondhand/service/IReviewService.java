package com.campus.secondhand.service;

import com.campus.secondhand.entity.Review;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 评价服务接口
 * 
 * 设计模式应用：
 * 1. 接口隔离原则：定义精简的评价相关操作接口
 * 2. 依赖倒置：Controller依赖此接口而非具体实现
 * 3. 观察者模式：添加评价后触发信用评分更新（通过Spring事件机制）
 */
public interface IReviewService extends IService<Review> {
    
    /**
     * 添加评价
     * @param review 评价信息
     * @return 是否添加成功
     */
    boolean addReview(Review review);
    
    /**
     * 根据被评价人查询评价列表
     * @param reviewedId 被评价人ID
     * @return 评价列表
     */
    List<Review> getReviewsByReviewed(Integer reviewedId);
    
    /**
     * 根据订单查询评价
     * @param orderId 订单ID
     * @return 评价信息
     */
    Review getReviewByOrder(Integer orderId);
    
    /**
     * 获取用户平均评分
     * @param userId 用户ID
     * @return 平均评分
     */
    Double getAverageRating(Integer userId);
}
