package com.smartgreen.processor;

import com.micer.core.event.Event;
import org.apache.avro.util.Utf8;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.HashMap;
import java.util.Map;


/**
 * 每15min统计一次能耗
 */
public class Min15StatisticsProcessor implements Processor<String, Event> {

    public static final String NAME = "min15statistics-processor";

    private ProcessorContext context; // 可以的得到流处理的上下文，init()函数中必须赋值

    private KeyValueStore<String, Event> dataStore;

    public static final String DATASTORE = "min15DataStore";

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
        dataStore = (KeyValueStore<String, Event>) context.getStateStore(DATASTORE);
    }

    /**
     * 实际上此处的deviceId是管理实体的id了
     * @param s
     * @param curr
     */
    @Override
    public void process(String s, Event curr) {
        System.out.println("in min15 curr = " + curr);
        String deviceId = curr.getDeviceConfigId().toString();
        Event pre = dataStore.get(deviceId);
        System.out.println("in min15 pre = " + pre);
        if (pre != null && curr.getTimestamp() > pre.getTimestamp()) {
            Event event = new Event();
            event.setEventId(pre.getEventId());
            event.setTimestamp(pre.getTimestamp());
            event.setDeviceProtocolId(pre.getDeviceProtocolId());
            event.setDeviceConfigId(pre.getDeviceConfigId());

            //System.out.println("in min15 pre.map = " + pre.getValues());
            //System.out.println("in min15 curr.map = " + curr.getValues());
            Utf8 key = new Utf8("000");
            int preV = Integer.parseInt(pre.getValues().get(key).toString());
           // System.out.println("int min15 preV = " + preV);
            for (CharSequence c : curr.getValues().keySet()) {
                System.out.println(c.getClass());
            }
            int currV = Integer.parseInt(curr.getValues().get(key).toString());

            //System.out.println("int min15 currV = " + currV);
            int delta = currV - preV;
            System.out.println("in min15: delta value = " + delta);
            Map<CharSequence, CharSequence> map = new HashMap<>();
            map.put(key, delta + "");
            event.setValues(map);
            context.forward(deviceId, event);
        }
        dataStore.put(deviceId, curr);
        context.commit();
    }

    @Override
    public void close() {

    }
}
