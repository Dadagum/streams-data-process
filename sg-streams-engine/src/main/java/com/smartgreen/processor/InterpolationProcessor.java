package com.smartgreen.processor;

import com.micer.core.event.Event.Event;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.concurrent.atomic.AtomicInteger;

public class InterpolationProcessor implements Processor<String, Event> {

    public static final String NAME = "interpolation-processor";

    private ProcessorContext context; // 可以的得到流处理的上下文，init()函数中必须赋值

    private static final AtomicInteger counter = new AtomicInteger(1);

    private String processorName;

    private KeyValueStore<String, Event> datastore;

    public InterpolationProcessor(String name) {
        this.processorName = name + "-" + counter.getAndIncrement();
    }

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public void process(String s, Event event) {
        System.out.println("get record " + s + " -> " + event);
        context.forward(s, event);
    }

    @Override
    public void close() {

    }
}
