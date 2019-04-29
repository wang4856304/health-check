package com.wj.controller;

import com.wj.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jun.wang
 * @title: HealthCheckController
 * @projectName ownerpro
 * @description: TODO
 * @date 2019/4/28 15:20
 */

@RestController
public class HealthCheckController {

    @Autowired
    private RegisterService registerService;

    @GetMapping(path = "/getAllHealth")
    public Object getAllHealth() {
        return registerService.getAllService();
    }
}
