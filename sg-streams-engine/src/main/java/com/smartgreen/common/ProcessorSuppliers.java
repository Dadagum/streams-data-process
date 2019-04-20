package com.smartgreen.common;

import com.micer.core.event.Event.Event;
import com.smartgreen.processor.InterpolationProcessor;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorSupplier;

public class ProcessorSuppliers {


    public static class InterpolationProcessorSupplier implements ProcessorSupplier<String, Event> {

        @Override
        public Processor<String, Event> get() {
            return new InterpolationProcessor(InterpolationProcessor.NAME);
        }
    }
}
