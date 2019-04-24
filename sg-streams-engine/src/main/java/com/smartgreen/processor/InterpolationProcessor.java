package com.smartgreen.processor;

import com.micer.core.event.Event;
import com.smartgreen.common.InterpolationUtils;
import com.smartgreen.model.Entity;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InterpolationProcessor implements Processor<String, Event> {

    public static final String NAME = "interpolation-processor";

    public static final String DATASTORE = "rawDataStore";

    private ProcessorContext context; // 可以的得到流处理的上下文，init()函数中必须赋值

    private static final AtomicInteger counter = new AtomicInteger(1);

    private KeyValueStore<String, Entity> dataStore;

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
        dataStore = (KeyValueStore<String, Entity>) context.getStateStore(DATASTORE);
    }

    @Override
    public void process(String s, Event event) {
        System.out.println("get record " + s + " -> " + event);
        // 得到前一条数据
        String uuid = event.getDeviceConfigId().toString();
        Entity pre = dataStore.get(uuid);
        System.out.println("pre = " + pre);
        // 实体的转换
        Entity curr = InterpolationUtils.convert(event);
        if (pre != null && curr.getRunAt() > pre.getRunAt()) {
            List<Entity> missing = InterpolationUtils.average(pre, curr);
            for (int i = 0; i < missing.size(); i++) {
                System.out.println("adding : " + missing.get(i));
                context.forward(uuid, missing.get(i));
            }
        }
        dataStore.put(uuid, curr);
        context.forward(uuid, curr);
        context.commit();
    }

    @Override
    public void close() {

    }
}
