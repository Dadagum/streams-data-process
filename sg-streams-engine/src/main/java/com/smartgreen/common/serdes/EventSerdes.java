package com.smartgreen.common.serdes;


import com.micer.core.event.Event;
import com.smartgreen.common.constant.Constant;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroDeserializer;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerializer;
import org.apache.kafka.common.serialization.Serde;

import java.util.HashMap;
import java.util.Map;

/**
 * 采集端传输模型Event的序列化工具类
 */
public class EventSerdes {

    public static final SpecificAvroSerializer<Event> serializer() {
        SpecificAvroSerializer<Event> deserializer = new SpecificAvroSerializer<>();
        Map<String, String> config = config();
        deserializer.configure(config, false);
        return deserializer;
    }

    public static final SpecificAvroDeserializer<Event> deserializer() {
        SpecificAvroDeserializer<Event> deserializer = new SpecificAvroDeserializer<>();
        Map<String, String> config = config();
        deserializer.configure(config, false);
        return deserializer;
    }

    private static Map<String, String> config() {
        Map<String, String> config = new HashMap<>();
        config.put("specific.avro.reader", "true");
        config.put("schema.registry.url", Constant.SCHEMA_URL);
        config.put("auto.register.schemas", "true");
        return config;
    }

    /**
     * addStateStore中需要
     * @return
     */
    public static Serde<Event> serde() {
        Serde<Event> serde = new SpecificAvroSerde<>();
        Map<String, String> config = new HashMap<>();
        config.put("schema.registry.url", Constant.SCHEMA_URL);
        serde.configure(config, false);
        return serde;
    }

}
