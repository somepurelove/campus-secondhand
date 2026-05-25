package com.campus.secondhand.event;

import com.campus.secondhand.entity.Review;
import org.springframework.context.ApplicationEvent;

/**
 * 评价事件类
 * 
 * 设计模式应用：观察者模式
 * 当评价被创建时，发布此事件通知所有监听者
 */
public class ReviewEvent extends ApplicationEvent {
    
    private final Review review;
    private final String eventType; // "CREATED", "UPDATED"
    
    /**
     * 构造评价事件
     * @param source 事件源
     * @param review 评价对象
     * @param eventType 事件类型
     */
    public ReviewEvent(Object source, Review review, String eventType) {
        super(source);
        this.review = review;
        this.eventType = eventType;
    }
    
    /**
     * 获取评价对象
     * @return 评价对象
     */
    public Review getReview() {
        return review;
    }
    
    /**
     * 获取事件类型
     * @return 事件类型
     */
    public String getEventType() {
        return eventType;
    }
}
