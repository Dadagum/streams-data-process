package com.smartgreen.model;

import java.util.Map;

/**
 * @Description TODO
 * @Author Honda
 * @Date 2019/4/24 16:45
 **/
public class ManageEntity {

    private String uuid;

    private Map<String, Double> compose;

    public ManageEntity(String uuid, Map<String, Double> compose) {
        this.uuid = uuid;
        this.compose = compose;
    }

    public ManageEntity() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Map<String, Double> getCompose() {
        return compose;
    }

    public void setCompose(Map<String, Double> compose) {
        this.compose = compose;
    }
}