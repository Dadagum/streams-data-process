package com.smartgreen.common;

import com.smartgreen.model.Event;
import com.smartgreen.processor.InterpolationProcessor;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorSupplier;

public class ProcessorSuppliers {

    private static final String INTERPOLATION_PROCESSOR_NAME = "InterpolationProcessor";

    public static class InterpolationProcessorSupplier implements ProcessorSupplier<String, Event> {

        @Override
        public Processor<String, Event> get() {
            return new InterpolationProcessor(INTERPOLATION_PROCESSOR_NAME);
        }
    }
}
