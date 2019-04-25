package com.smartgreen.common.constant;

public class Constant {

    /**
     * Confluent , Kafka Streams的各种配置
     */
    public static final String IP = "http://127.0.0.1";
    public static final String SERVER = IP + ":9092";
    public static final String APPLICATION = "event-application";
    public static final String CLIENT_ID = "interpolation-processor-client";
    public static final String SCHEMA_URL = IP + ":8081";

    /**
     * Source Topic
     */
    public static final String INPUT_TOPIC = "yzemeter-test2";
    //public static final String INPUT_TOPIC = "test-event-input-topic2";

    /**
     * Sink Topic
     */
//    public static final String MIN_15_TOPIC = "min-15-output-topic";
    public static final String OUTPUT_HOUR_TOPIC = "yzemeter-hour-topic";
    public static final String OUTPUT_DAY_TOPIC = "yzemeter-day-topic";
    public static final String OUTPUT_MONTH_TOPIC = "yzemeter-month-topic";
    public static final String OUTPUT_YEAR_TOPIC = "yzemeter-year-topic";
    public static final String RAW_OUTPUT_TOPIC = "test-event-output-topic";

    /**
     * Source Processor
     */
    public static final String SOURCE_PROCESSOR = "SOURCE_PROCESSOR";

    /**
     * Sink Processor
     */
    public static final String SINK_HOUR_PROCESSOR = "sink-hour-processor";
    public static final String SINK_DAY_PROCESSOR = "sink-day-processor";
    public static final String SINK_MONTH_PROCESSOR = "sink-month-processor";
    public static final String SINK_YEAR_PROCESSOR = "sink-year-processor";
    public static final String SINK_RAW_PROCESSOR = "sink-raw-processor";


    /**
     * 采集程序间隔：１5min
     */
    @Deprecated
    public static final int INTERVAL = 900000;
}
