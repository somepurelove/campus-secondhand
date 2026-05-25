package com.campus.secondhand.listener;

import com.campus.secondhand.event.ReviewEvent;
import com.campus.secondhand.service.CreditScoreCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 评价事件监听器
 * 
 * 设计模式应用：观察者模式
 * 监听评价事件并触发相应的业务逻辑
 */
@Component
public class ReviewEventListener {
    
    private final CreditScoreCalculator creditScoreCalculator;
    
    /**
     * 构造监听器
     * @param creditScoreCalculator 信用评分计算器
     */
    @Autowired
    public ReviewEventListener(CreditScoreCalculator creditScoreCalculator) {
        this.creditScoreCalculator = creditScoreCalculator;
    }
    
    /**
     * 处理评价创建事件
     * @param event 评价事件
     */
    @EventListener
    public void handleReviewCreated(ReviewEvent event) {
        if ("CREATED".equals(event.getEventType())) {
            // 新评价创建时更新被评价人的信用评分
            creditScoreCalculator.updateCreditScoreAfterReview(event.getReview());
            System.out.println("收到新评价事件，已更新用户" + event.getReview().getReviewedId() + "的信用评分");
        }
    }
}
