package com.smartgreen.common;

public class Constant {

    public static final String IP = "http://127.0.0.1";

    public static final String SERVER = IP + ":9092";

    public static final String APPLICATION = "event-application";

    public static final String CLIENT_ID = "interpolation-processor-client";

    //public static final String INPUT_TOPIC = "test-event-input-topic2";
    public static final String INPUT_TOPIC = "yzemetor-test2";

    /**
     * 记录“原始数据”的topic
     */
    public static final String RAW_OUTPUT_TOPIC = "test-event-output-topic";

    /**
     * 记录分时后的topic
     */
    public static final String MIN_15_TOPIC = "min-15-output-topic";

    public static final String SCHEMA_URL = IP + ":8081";

    /**
     * 采集程序间隔：１5min
     */
    public static final int INTERVAL = 900000;
}
