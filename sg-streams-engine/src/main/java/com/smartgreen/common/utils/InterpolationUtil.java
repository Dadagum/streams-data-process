package com.smartgreen.common.utils;

import com.micer.core.event.Event;
import com.smartgreen.common.constant.Constant;
import com.smartgreen.model.Entity;
import org.apache.avro.util.Utf8;

import java.util.ArrayList;
import java.util.List;

public class InterpolationUtil {

    /**
     * 和采集端商量好的从values中取出来的key
     */
    private static final Utf8 POWER_STR = new Utf8("powerString");
    private static final Utf8 ACTUAL_TIME = new Utf8("actualTime");

    /**
     * 采用平均插值
     */
    public static List<Entity> average(Entity start, Entity end) {
        List<Entity> result = new ArrayList<>();
        //System.out.println("delta = " + (end.getTimestamp() - start.getTimestamp()));
        int cnt = Math.toIntExact((end.getRunAt() - start.getRunAt()) / Constant.INTERVAL);
        if (cnt != 0) {
            double deltaValue = (end.getValue() - start.getValue()) / cnt;
            //System.out.println("pre = " + preV + ", currV = " + currV + ", cnt = " + cnt);
            for (int i = 1; i < cnt; i++) {
                Entity miss = Entity.newBuilder()
                        .setAnomaly(false)
                        .setOriginal(false)
                        .setId(0)
                        .setUuid(start.getUuid())
                        .setRunAt(start.getRunAt() + i * Constant.INTERVAL)
                        .setValue(start.getValue() + i * deltaValue)
                        .build();

                result.add(miss);
            }
        }
        return result;
    }

    /**
     * 将采集程序使用的数据类型Event转换为项目采用的类型
     */
    public static Entity convert(Event event) {
        int value = Integer.parseInt(event.getValues().get(POWER_STR).toString());
        return Entity.newBuilder()
                .setAnomaly(false)
                .setOriginal(true)
                .setId(0)
                .setUuid(event.getDeviceConfigId())
                .setRunAt(event.getTimestamp())
                .setValue(value)
                .build();
    }
}
