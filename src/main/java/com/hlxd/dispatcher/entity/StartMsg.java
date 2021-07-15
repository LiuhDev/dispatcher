package com.hlxd.dispatcher.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ：liuhao
 * @date ：Created in 2021/7/12
 */
@Data
@AllArgsConstructor
public class StartMsg {

    private String port;

    private String user;

    private int sign;

    private String errMsg;
}
