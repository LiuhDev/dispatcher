package com.hlxd.dispatcher.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.hlxd.dispatcher.common.CodeMsg;
import com.hlxd.dispatcher.common.Result;
import com.hlxd.dispatcher.entity.AppInfo;
import com.hlxd.dispatcher.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/dispatcher")
public class DispatcherController {


    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/registerOrUpdate")
    public Result<?> registerOrUpdate(@RequestBody List<AppInfo> list) {

        List<AppInfo> appList = JSONUtil.toList((JSONArray) redisUtil.get("app"), AppInfo.class);
        if (appList == null) {
            appList = new ArrayList<>();
        }

        for (AppInfo s : list) {
            int sign = 0;
            for (AppInfo s1 : appList) {
                if (s.getIp().equals(s1.getIp()) && s.getPort().equals(s1.getPort())) {
                    s1.setStatus(s.getStatus());
                    sign = 1;
                }
            }
            if (sign == 0) {
                appList.add(s);
            }
        }
        JSONArray jsonArray = JSONUtil.parseArray(appList);

        return Result.success(redisUtil.set("app", jsonArray, -1));
    }

    @GetMapping("/getAppList")
    public Result<?> getAppList() {

        List<AppInfo> appList = JSONUtil.toList((JSONArray) redisUtil.get("app"), AppInfo.class);
        return Result.success(appList);
    }

    @PostMapping("/launch")
    public Result<?> launch() {

        List<AppInfo> appList = JSONUtil.toList((JSONArray) redisUtil.get("app"), AppInfo.class);
        for (AppInfo s : appList) {
            if (s.getStatus() == 0) {
                //todo 启动应用

                s.setStatus(1);
                return Result.success(s);
            }
        }
        return Result.error(CodeMsg.BUSY);

    }


}
