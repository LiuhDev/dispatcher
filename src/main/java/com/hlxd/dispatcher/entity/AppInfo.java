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

    private String ip;

    private String port;

    private int status;
}
