package com.campus.secondhand.event;

import com.campus.secondhand.entity.Dispute;
import org.springframework.context.ApplicationEvent;

/**
 * 纠纷事件类
 *
 * 设计模式应用：观察者模式
 * 当纠纷被处理时，发布此事件通知所有监听者
 */
public class DisputeEvent extends ApplicationEvent {

    private final Dispute dispute;
    private final String eventType; // "RESOLVED", "REJECTED"

    /**
     * 构造纠纷事件
     * @param source 事件源
     * @param dispute 纠纷对象
     * @param eventType 事件类型
     */
    public DisputeEvent(Object source, Dispute dispute, String eventType) {
        super(source);
        this.dispute = dispute;
        this.eventType = eventType;
    }

    /**
     * 获取纠纷对象
     * @return 纠纷对象
     */
    public Dispute getDispute() {
        return dispute;
    }

    /**
     * 获取事件类型
     * @return 事件类型
     */
    public String getEventType() {
        return eventType;
    }
}
