package com.smartgreen;

import com.micer.core.event.Event;
import com.smartgreen.service.EntityService;
import org.apache.avro.util.Utf8;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConsumerRunner{

    private static final String IP = "http://127.0.0.1";

    private static final String SERVER = IP + ":9092";

    private static final String SCHEMA_URL = IP + ":8081";

    private static final String groupId = "hongda-group";

    public static final String RAW_OUTPUT_TOPIC = "test-event-output-topic";

    public static final String MIN_15_TOPIC = "min-15-output-topic";

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
        List<String> topics = new ArrayList<>();
        topics.add(RAW_OUTPUT_TOPIC);
        topics.add(MIN_15_TOPIC);
        consumer.subscribe(topics);
        EntityService service = new EntityService();

        while (true) {
            ConsumerRecords<String, Event> data = consumer.poll(10);
            for (ConsumerRecord<String, Event> record : data) {
                String key = record.key();
                Event event = record.value();
                Utf8 u = new Utf8("000");
                String value = event.getValues().get(u).toString();
                System.out.printf("consumer get (key = %s, value = {deviceConfigId = %s, timestamp = %s, value = %s} from topic : %s)\n", key, event.getDeviceConfigId(), event.getTimestamp(), value, record.topic());

                //持久化到数据库
                service.saveEntity(event);
            }
            // commit offset
            consumer.commitAsync();
        }
    }

}
