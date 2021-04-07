package com.cq.cardutil.base.event;

import java.util.Date;

/**
 * @author chenqi
 * @date 2020/11/16 14:40
 */
public interface Event<T> {
    Object getSource();

    Date getWhen();

    String getMessage();

    T getData();
    /**
     * 事件回调方法
     */
    void callback();
}
