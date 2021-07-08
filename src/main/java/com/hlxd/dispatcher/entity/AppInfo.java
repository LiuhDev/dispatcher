package com.hlxd.dispatcher.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ：liuhao
 * @date ：Created in 2021/7/6
 */
@Data
@AllArgsConstructor
public class AppInfo implements Serializable {

    public final static int IDLE = 0;
    public final static int BUSY = 1;
    public final static int START = 2;
    public final static int STOP = 3;

    private String ip;

    private String port;

    private int status;

    private String url;
}
