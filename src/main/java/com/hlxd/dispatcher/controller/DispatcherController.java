package com.hlxd.dispatcher.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.hlxd.dispatcher.common.CodeMsg;
import com.hlxd.dispatcher.common.Result;
import com.hlxd.dispatcher.entity.AppInfo;
import com.hlxd.dispatcher.entity.Test;
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

    @PostMapping("/launch")
    public Result<?> launch() {

        List<AppInfo> appList = JSONUtil.toList((JSONArray) redisUtil.get("app"), AppInfo.class);
        for (AppInfo s : appList) {
            if (s.getStatus() == AppInfo.IDLE) {
                //todo 启动应用

                s.setStatus(AppInfo.BUSY);
                JSONArray jsonArray = JSONUtil.parseArray(appList);
                return Result.success(redisUtil.set("app", jsonArray, -1));
            }
        }
        return Result.error(CodeMsg.BUSY);

    }


    @PostMapping("/test")
    public Result<?> test() throws IOException {
        Socket socket = SocketServer.socketList.get(0);
//        Socket socket = new Socket("127.0.0.1", 3000);
        SocketServer.sendMessage(socket, "hahahaover");
        return Result.success();
    }

}
