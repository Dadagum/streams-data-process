package com.smartgreen.model;

/**
 * 计量实体 / 管理实体
 */
public class Entity {

    /**
     * 数据库主键
     */
    private Integer id;

    /**
     * 设备的uuid
     */
    private String uuid;

    /**
     * 设备在某个时间维度的统计值/某时刻的表值
     */
    private Double value;

    /**
     * 时刻，如果是分维度的表那么就是时间维度的开始时刻
     */
    private Long runAt;

    /**
     * 是否为原来的值
     * 1：就是从设备读出的原值
     * 0：插值处理过
     */
    private Boolean original;

    /**
     * 业务错误
     * 1：业务错误
     * 0：没有错误
     * 一般来说，原来的表是肯定不会有-1的，-1转换后有的
     * 例如，统计一周的数据，但是发现数据缺失，那么就有-1的业务错误，顺便带着value为-1
     */
    private Boolean anomaly;

    public Entity() {
    }

    public Entity(String uuid, Double value, Long runAt, Boolean original, Boolean anomaly) {
        this.uuid = uuid;
        this.value = value;
        this.runAt = runAt;
        this.original = original;
        this.anomaly = anomaly;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Long getRunAt() {
        return runAt;
    }

    public void setRunAt(Long runAt) {
        this.runAt = runAt;
    }

    public Boolean getOriginal() {
        return original;
    }

    public void setOriginal(Boolean original) {
        this.original = original;
    }

    public Boolean getAnomaly() {
        return anomaly;
    }

    public void setAnomaly(Boolean anomaly) {
        this.anomaly = anomaly;
    }
}
