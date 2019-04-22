package com.smartgreen;

import com.micer.core.event.Event.Event;
import com.smartgreen.model.Entity;
import com.smartgreen.service.EntityService;
import org.apache.avro.util.Utf8;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

public class ConsumerRunner{

    private static final String IP = "http://127.0.0.1";

    private static final String SERVER = IP + ":9092";

    private static final String SCHEMA_URL = IP + ":8081";

    private static final String groupId = "hongda-group";

   // private static final String TOPIC = "test-event-output-topic";
    //private static final String TOPIC = "min-15-output-topic";
    private static final String TOPIC = "test-event-input-topic2";

    private static final Properties props = new Properties();

    static {
        props.put("bootstrap.servers", SERVER);
        props.put("group.id", groupId);
        props.put("auto.commit.enable", "false");
        props.put("auto.offset.reset", "latest");
        // props.put("compression.type", "lz4"); // no needed, auto detected by
        // kafka when consuming
        props.put("schema.registry.url", SCHEMA_URL);
        props.put("specific.avro.reader", true);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // 使用avro反序列化
        props.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
    }


    public static void main(String[] args) {
        KafkaConsumer<String, Event> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(TOPIC));
        EntityService service = new EntityService();

        while (true) {
            ConsumerRecords<String, Event> data = consumer.poll(10);
            for (ConsumerRecord<String, Event> record : data) {
                String key = record.key();
                Event event = record.value();
                Utf8 u = new Utf8("000");
                String value = event.getValues().get(u).toString();
                System.out.printf("consumer get (key = %s, value = {deviceConfigId = %s, timestamp = %s, value = %s})\n", key, event.getDeviceConfigId(), event.getTimestamp(), value);

                //持久化到数据库
                service.saveEntity(event);
            }
            // commit offset
            consumer.commitAsync();
        }
    }

}
