package com.smartgreen.common;


import io.confluent.kafka.streams.serdes.avro.SpecificAvroDeserializer;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerializer;
import org.apache.kafka.common.serialization.Serde;
import com.smartgreen.model.Event;

import java.util.HashMap;
import java.util.Map;

public class SerdesUtils {

    public static final SpecificAvroSerializer<Event> createEventSerializer() {
        SpecificAvroSerializer<Event> deserializer = new SpecificAvroSerializer<>();
        Map<String, String> config = createSerdeConfig();
        deserializer.configure(config, false);
        return deserializer;
    }

    public static final SpecificAvroDeserializer<Event> createEventDeserializer() {
        SpecificAvroDeserializer<Event> deserializer = new SpecificAvroDeserializer<>();
        Map<String, String> config = createSerdeConfig();
        deserializer.configure(config, false);
        return deserializer;
    }

    public static Serde<Event> createEventSerde() {
        Serde<Event> serde = new SpecificAvroSerde<>();
        Map<String, String> config = new HashMap<>();
        config.put("schema.registry.url", Constant.SCHEMA_URL);
        serde.configure(config, false);
        return serde;
    }



    private static Map<String, String> createSerdeConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("specific.avro.reader", "true");
        config.put("schema.registry.url", Constant.SCHEMA_URL);
        config.put("auto.register.schemas", "true");
        return config;
    }
}
