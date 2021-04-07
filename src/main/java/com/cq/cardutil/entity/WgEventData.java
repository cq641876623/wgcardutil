package com.cq.cardutil.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author chenqi
 * @date 2020/11/16 15:17
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WgEventData {
    private String card;
    private int inOrOut;
    private String ip;
    private String serialNumber;
    private int doorNum;

}
