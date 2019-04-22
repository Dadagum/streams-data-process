package com.smartgreen;

import com.micer.core.event.Event.Event;
import com.smartgreen.utils.UUIDUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerRunner {

    private static final String IP = "http://127.0.0.1";

    private static final String SERVER = IP + ":9092";

    private static final String INPUT_TOPIC = "test-event-input-topic2";

    private static final String SCHEMA_URL = IP + ":8081";

    /**
     * kafka producer 的配置
     */
    private static final Properties props = new Properties();

    static {
        props.put("bootstrap.servers", SERVER);
        props.put("schema.registry.url", SCHEMA_URL);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // 使用avro序列化
        props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        props.put("acks", "all");
        props.put("retries", 0);
    }

    /**
     * 模拟生成一个Event
     * @return
     */
    private static Event nextEvent() {
        Event event = new Event();
        event.setEventId(UUIDUtils.get());
        event.setDeviceConfigId("YZ001");
        event.setDeviceProtocolId("011001");
        long timestamp = System.currentTimeMillis();
        event.setTimestamp(timestamp);
        Map<CharSequence, CharSequence> values = new HashMap<CharSequence, CharSequence>();
        values.put("000", "000100");
        event.setValues(values);
        return event;
    }

    /**
     * 启动生产者程序
     * @param args
     */
    public static void main(String[] args) {
        // 生产者配置
        KafkaProducer<String, Event> producer = new KafkaProducer<>(props);
        try {
            int[] intervals = {0, 900000};
            Event event = nextEvent();
            long st = event.getTimestamp();
            for (int i = 0; i < intervals.length; i++) {
                event.setTimestamp(st + intervals[i]);
                Map<CharSequence, CharSequence> map = event.getValues();
                String cs = map.get("000").toString();
                int value = Integer.parseInt(cs) + 6;
                map.put("000", value + "");
                event.setValues(map);
                ProducerRecord<String, Event> record = new ProducerRecord<>(INPUT_TOPIC, "${eventId}", event);
                RecordMetadata metaData = producer.send(record).get();
                // 消息发送情况
                System.out.printf("sent record(key=%s value=%s) meta(partition=%d, offset=%d) \n",
                        record.key(), record.value(), metaData.partition(), metaData.offset());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }
}
