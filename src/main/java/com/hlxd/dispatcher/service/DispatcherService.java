package com.hlxd.dispatcher.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.hlxd.dispatcher.common.Result;
import com.hlxd.dispatcher.entity.AppInfo;
import com.hlxd.dispatcher.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：liuhao
 * @date ：Created in 2021/7/8
 */
@Service
public class DispatcherService {

    @Autowired
    private RedisUtil redisUtil;

    public Boolean registerOrUpdate(@RequestBody List<AppInfo> list) {

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

        return redisUtil.set("app", jsonArray, -1);
    }
}
