package com.wj.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wj.service.RegisterService;
import com.wj.service.entity.Health;
import com.wj.utils.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jun.wang
 * @title: RegisterServiceImpl
 * @projectName ownerpro
 * @description: TODO
 * @date 2019/4/28 14:25
 */

@Service
public class RegisterServiceImpl implements RegisterService {

    private static Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);

    @Autowired
    RestTemplate restTemplate;

    @Value("${register-center.url}")
    private String url;

    @Override
    public List<Health> getAllService() {
        List<Health> healthList = new ArrayList<>();

        Map<String, List<String>> resultMap = getService();

        Set<Map.Entry<String, List<String>>> set = resultMap.entrySet();
        set.stream().map(entry->{
            healthList.add(getHealth(entry));
            return 0;
        }).collect(Collectors.toList());
        return healthList;
    }

    private Health getHealth(Map.Entry<String, List<String>> entry) {
        Health health = new Health();
        //List<JSONObject> healthDescList = new ArrayList<>();
        String instanceName = entry.getKey();
        health.setInstanceName(instanceName);
        /*List<String> urlList = entry.getValue();
        urlList.stream().map(url->{
            if (url.endsWith("/")) {
                url = url + "health";
            }
            else {
                url = url + "/health";
            }
            String result = HttpClientUtil.httpGetRequest(url);
            JSONObject resultJson = JSONObject.parseObject(result);
            healthDescList.add(resultJson);
            return 0;
        }).collect(Collectors.toList());*/
        String result = restTemplate.getForObject("http://" + instanceName + "/health", String.class);
        JSONObject resultJson = JSONObject.parseObject(result);
        if (!"UP".equalsIgnoreCase(resultJson.getString("status"))) {
            logger.info(instanceName +  " is down");
            //TODO    //短信或邮件通知
        }
        health.setHealthDesc(resultJson);
        return health;
    }


    private Map<String, List<String>> getService() {
        Map<String, List<String>> map = new HashMap<>();
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("Accept", "application/json");
        String result = HttpClientUtil.httpGetRequestWithHeader(url, header);
        JSONObject resJson = JSONObject.parseObject(result);
        JSONObject appsJson = resJson.getJSONObject("applications");
        if (appsJson != null) {
            JSONArray appJsonArr = appsJson.getJSONArray("application");
            for (int i = 0; i < appJsonArr.size(); i++) {
                String name = appJsonArr.getJSONObject(i).getString("name");
                if ("health-check".equalsIgnoreCase(name)) {
                    continue;
                }
                JSONArray instJsonArr = appJsonArr.getJSONObject(i).getJSONArray("instance");
                List<String> serviceUrls = new ArrayList<>();
                for (int j = 0; j < instJsonArr.size(); j++) {
                    JSONObject instJson = instJsonArr.getJSONObject(j);
                    String serviceUrl = instJson.getString("homePageUrl");
                    serviceUrls.add(serviceUrl);
                }
                map.put(name, serviceUrls);
            }
        }
        return map;
    }
}
