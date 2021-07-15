package com.hlxd.dispatcher.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：liuhao
 * @date ：Created in 2021/7/12
 */
@Data
@AllArgsConstructor
public class Message implements Serializable {

    public final static int START = 0;
    public final static int CLOSE = 1;

    private int sign;

    private List<AppInfo> appList;


}
