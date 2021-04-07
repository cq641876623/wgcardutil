package com.cq.cardutil.base.event;

/**
 * @author chenqi
 * @date 2020/11/16 14:39
 */
public interface EventSource {
    /**
     * 增加监听器
     * @param listener
     */
    void addListener(EventListener listener);

    /**
     * 通知监听器
     */
    void notifyListener(Event event);
}
