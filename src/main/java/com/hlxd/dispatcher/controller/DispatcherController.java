package com.hlxd.dispatcher.controller;

import com.hlxd.dispatcher.common.CodeMsg;
import com.hlxd.dispatcher.common.Result;
import com.hlxd.dispatcher.entity.AppInfo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dispatcher")
public class DispatcherController {

    List<AppInfo> appList = new ArrayList<>();

    @PostMapping ("/registerOrUpdate")
    public Result<?> registerOrUpdate(@RequestBody List<AppInfo> list) {

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
        return Result.success();
    }

    @GetMapping ("/getAppList")
    public Result<?> getAppList() {

        return Result.success(appList);
    }

    @PostMapping("/launch")
    public Result<?> launch(){

        for (AppInfo s : appList) {
            if (s.getStatus() == 0) {
                //todo 启动应用

                s.setStatus(1);
                return Result.success(s);
            }
        }
        return Result.error(CodeMsg.BUSY);

    }

//    @PostMapping("/stop/{}")
    

}
