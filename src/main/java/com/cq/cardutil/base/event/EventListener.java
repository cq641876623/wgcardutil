package com.cq.cardutil.base.event;

/**
 * @author chenqi
 * @date 2020/11/16 14:40
 */
public interface EventListener<T> {

    void handle(Event<T> event);
}
