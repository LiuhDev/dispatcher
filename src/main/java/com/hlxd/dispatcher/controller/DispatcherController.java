package com.hlxd.dispatcher.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.hlxd.dispatcher.common.CodeMsg;
import com.hlxd.dispatcher.common.Result;
import com.hlxd.dispatcher.entity.AppInfo;
import com.hlxd.dispatcher.entity.RedisSocket;
import com.hlxd.dispatcher.entity.StartMsg;
import com.hlxd.dispatcher.service.SocketServer;
import com.hlxd.dispatcher.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/dispatcher")
public class DispatcherController {


    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/launch")
    public Result<?> launch(String user) {

        List<AppInfo> appList = JSONUtil.toList((JSONArray) redisUtil.get("app"), AppInfo.class);
        if (CollectionUtil.isEmpty(appList)) {
            return Result.error(CodeMsg.BUSY);
        }
        for (AppInfo s : appList) {
            if (s.getStatus() == AppInfo.IDLE) {
                //启动应用
                synchronized (SocketServer.socketList) {
                    for (Socket socket : SocketServer.socketList) {
                        if (socket.getInetAddress().getHostAddress().equals(s.getIp())) {
                            StartMsg startMsg = new StartMsg(s.getPort(), user, 0, null);
                            SocketServer.sendMessage(socket, JSONUtil.toJsonStr(startMsg) + "over");
                        }
//                        StartMsg startMsg = new StartMsg(s.getPort(), user, 0, null);
//                        SocketServer.sendMessage(socket, JSONUtil.toJsonStr(startMsg) + "over");
                    }
                }
                s.setStatus(AppInfo.START);
                JSONArray jsonArray = JSONUtil.parseArray(appList);
                return Result.success(redisUtil.set("app", jsonArray, -1));
            }
        }
        return Result.error(CodeMsg.BUSY);

    }


    @PostMapping("/test")
    public Result<?> test() throws IOException {
        RedisSocket rs = (RedisSocket) redisUtil.get("127.0.0.1");
//        Socket socket = new Socket("127.0.0.1", 3000);
        SocketServer.sendMessage(rs.getSocket(), "hahahaover");
        return Result.success();
    }

}
