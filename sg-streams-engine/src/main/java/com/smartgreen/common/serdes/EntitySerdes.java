package com.smartgreen.common.serdes;

import com.smartgreen.common.constant.Constant;
import com.smartgreen.model.Entity;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroDeserializer;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerializer;
import org.apache.kafka.common.serialization.Serde;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 本项目所用数据模型Entity用到的序列化工具类
 * @Author Honda
 * @Date 2019/4/25 9:42
 **/
public class EntitySerdes {

    public static final SpecificAvroSerializer<Entity> serializer() {
        SpecificAvroSerializer<Entity> deserializer = new SpecificAvroSerializer<>();
        Map<String, String> config = config();
        deserializer.configure(config, false);
        return deserializer;
    }

    public static final SpecificAvroDeserializer<Entity> deserializer() {
        SpecificAvroDeserializer<Entity> deserializer = new SpecificAvroDeserializer<>();
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
    public static Serde<Entity> serde() {
        Serde<Entity> serde = new SpecificAvroSerde<>();
        Map<String, String> config = new HashMap<>();
        config.put("schema.registry.url", Constant.SCHEMA_URL);
        serde.configure(config, false);
        return serde;
    }
}
