package com.wj.service.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author jun.wang
 * @title: Health
 * @projectName ownerpro
 * @description: TODO
 * @date 2019/4/28 15:02
 */

@Data
@NoArgsConstructor
public class Health {
    private String instanceName;
    private JSONObject healthDesc;
}
