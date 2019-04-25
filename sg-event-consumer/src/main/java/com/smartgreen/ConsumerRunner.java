package com.smartgreen;

import com.micer.core.event.Event;
import com.smartgreen.model.Entity;
import com.smartgreen.service.EntityService;
import org.apache.avro.util.Utf8;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConsumerRunner{

    // consumer配置
    private static final String IP = "http://127.0.0.1";
    private static final String SERVER = IP + ":9092";
    private static final String SCHEMA_URL = IP + ":8081";
    private static final String groupId = "hongda-group";

    /**
     * 和采集端商量好的从values中取出来的key
     */
    private static final Utf8 POWER_STR = new Utf8("powerString");
    private static final Utf8 ACTUAL_TIME = new Utf8("actualTime");

    /**
     * Kafka Streams处理完数据的topic
     */
    public static final String OUTPUT_HOUR_TOPIC = "yzemeter-hour-topic";
    public static final String OUTPUT_DAY_TOPIC = "yzemeter-day-topic";
    public static final String OUTPUT_MONTH_TOPIC = "yzemeter-month-topic";
    public static final String OUTPUT_YEAR_TOPIC = "yzemeter-year-topic";
    public static final String RAW_OUTPUT_TOPIC = "test-event-output-topic";

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
        KafkaConsumer<String, Entity> consumer = new KafkaConsumer<>(props);
        // 订阅输出topic
        List<String> topics = new ArrayList<>();
        topics.add(OUTPUT_HOUR_TOPIC);
        topics.add(OUTPUT_DAY_TOPIC);
        topics.add(OUTPUT_MONTH_TOPIC);
        topics.add(OUTPUT_YEAR_TOPIC);
        topics.add(RAW_OUTPUT_TOPIC);
        consumer.subscribe(topics);

        EntityService service = new EntityService();
        while (true) {
            ConsumerRecords<String, Entity> data = consumer.poll(10);
            for (ConsumerRecord<String, Entity> record : data) {
                String key = record.key();
                Entity entity = record.value();
                System.out.printf("consumer get (key = %s, value = {uuid = %s, run_at = %d, value = %f} from topic : %s)\n", key, entity.getUuid(), entity.getRunAt(), entity.getValue(), record.topic());
                service.saveEntity(entity, record.topic());
            }
            // commit offset
            consumer.commitAsync();
        }
    }


}
