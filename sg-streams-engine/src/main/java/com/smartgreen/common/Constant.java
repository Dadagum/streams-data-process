package com.smartgreen.common;

public class Constant {

    public static final String IP = "http://127.0.0.1";

    public static final String SERVER = IP + ":9092";

    public static final String APPLICATION = "event-application";

    public static final String CLIENT_ID = "interpolation-processor-client";

    public static final String INPUT_TOPIC = "test-event-input-topic";

    public static final String OUTPUT_TOPIC = "test-event-output-topic";

    public static final String SCHEMA_URL = IP + ":8081";

    /**
     * 采集程序间隔
     */
    public static final long INTERVAL = 900L;
}
