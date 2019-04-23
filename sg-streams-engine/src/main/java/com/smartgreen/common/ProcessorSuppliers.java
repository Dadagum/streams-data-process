package com.smartgreen.common;

import com.micer.core.event.Event;
import com.smartgreen.processor.InterpolationProcessor;
import com.smartgreen.processor.Measure2ManageProcessor;
import com.smartgreen.processor.Min15StatisticsProcessor;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorSupplier;

/**
 * 创建拓扑时每一个processor的创建需要提供ProcessorSupplier，所以才单独有此类
 */
public class ProcessorSuppliers {


    public static class InterpolationProcessorSupplier implements ProcessorSupplier<String, Event> {

        @Override
        public Processor<String, Event> get() {
            return new InterpolationProcessor(InterpolationProcessor.NAME);
        }
    }

    public static class Measure2ManageProcessorSupplier implements ProcessorSupplier<String, Event> {

        @Override
        public Processor<String, Event> get() {
            return new Measure2ManageProcessor();
        }
    }


    public static class Min15StatisticsProcessorSupplier implements ProcessorSupplier<String, Event> {

        @Override
        public Processor<String, Event> get() {
            return new Min15StatisticsProcessor();
        }
    }
}
