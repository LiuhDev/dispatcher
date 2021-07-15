package com.hlxd.dispatcher.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.net.Socket;

/**
 * @author ：liuhao
 * @date ：Created in 2021/7/12
 */
@Data
@AllArgsConstructor
public class RedisSocket implements Serializable {

    private Socket socket;
}
