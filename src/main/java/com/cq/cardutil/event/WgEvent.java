package com.cq.cardutil.event;

import com.cq.cardutil.base.event.Event;
import com.cq.cardutil.entity.WgEventData;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chenqi
 * @date 2020/11/16 15:14
 */
@NoArgsConstructor
public class WgEvent implements Event<WgEventData> {


    private WgEventData data;

    private Object source;

    private Date when;

    private String message;

    public WgEvent(WgEventData data, Object source, String message) {
        this.data = data;
        this.source = source;
        this.message = message;
        this.when=new Date();
    }

    public Object getSource() {
        return source;
    }

    public Date getWhen() {
        return when;
    }

    public String getMessage() {
        return this.message;
    }

    public WgEventData getData() {
        return this.data;
    }

    public void callback() {

    }
}
