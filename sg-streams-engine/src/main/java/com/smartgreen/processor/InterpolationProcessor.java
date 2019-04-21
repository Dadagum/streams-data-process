package com.smartgreen.processor;

import com.micer.core.event.Event.Event;
import com.smartgreen.common.InterpolationUtils;
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

    private String processorName;

    private KeyValueStore<String, Event> dataStore;

    public InterpolationProcessor(String name) {
        this.processorName = name + "-" + counter.getAndIncrement();
    }

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
        dataStore = (KeyValueStore<String, Event>) context.getStateStore(DATASTORE);
    }

    @Override
    public void process(String s, Event curr) {
        System.out.println("get record " + s + " -> " + curr);
        String deviceId = curr.getDeviceConfigId().toString();
        Event pre = dataStore.get(deviceId);
        System.out.println("pre = " + pre);
        if (pre != null && curr.getTimestamp() > pre.getTimestamp()) {
            List<Event> missing = InterpolationUtils.average(pre, curr);
            for (int i = 0; i < missing.size(); i++) {
                System.out.println("adding : " + missing.get(i));
                context.forward(deviceId, missing.get(i));
            }
        }
        dataStore.put(deviceId, curr);
        context.forward(deviceId, curr);
        context.commit();
    }

    @Override
    public void close() {

    }
}
