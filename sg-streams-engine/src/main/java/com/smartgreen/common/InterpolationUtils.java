package com.smartgreen.common;

import com.micer.core.event.Event.Event;
import org.apache.avro.util.Utf8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterpolationUtils {

    /**
     * 采用平均插值
     */
    public static List<Event> average(Event start, Event end) {
        List<Event> result = new ArrayList<>();
        Utf8 key = new Utf8("000");
        int preV = Integer.parseInt(start.getValues().get(key).toString());
        int currV = Integer.parseInt(end.getValues().get(key).toString());
        System.out.println("delta = " + (end.getTimestamp() - start.getTimestamp()));
        int cnt = Math.toIntExact((end.getTimestamp() - start.getTimestamp()) / Constant.INTERVAL);
        if (cnt != 0) {
            int deltaValue = (currV - preV) / cnt;
            System.out.println("pre = " + preV + ", currV = " + currV + ", cnt = " + cnt);
            for (int i = 1; i < cnt; i++) {
                Event event = new Event();
                event.setDeviceConfigId(start.getDeviceConfigId());
                event.setDeviceProtocolId(start.getDeviceProtocolId());
                event.setTimestamp(start.getTimestamp() + i * Constant.INTERVAL);
                event.setEventId(start.getEventId());
                Map<CharSequence, CharSequence> map = new HashMap<>();
                int value = preV + deltaValue * i;
                map.put("000", value + "");
                event.setValues(map);
                result.add(event);
            }
        }
        return result;
    }
}
