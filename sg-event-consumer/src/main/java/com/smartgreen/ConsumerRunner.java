package com.smartgreen;

import com.smartgreen.model.Entity;
import com.smartgreen.db.EntityDao;
import com.smartgreen.quartz.jobs.AggregateManageEntityJob;
import com.smartgreen.quartz.jobs.Constant;
import com.smartgreen.service.EntityService;
import org.apache.avro.util.Utf8;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.*;

public class ConsumerRunner{

    // consumer配置
    private static final String IP = "http://127.0.0.1";
    private static final String SERVER = IP + ":9092";
    private static final String SCHEMA_URL = IP + ":8081";
    private static final String groupId = "hongda-group";

    /**
     * DEBUG
     * 和采集端商量好的从values中取出来的key
     */
    private static final Utf8 POWER_STR = new Utf8("powerString");
    private static final Utf8 ACTUAL_TIME = new Utf8("actualTime");

    /**
     * Kafka Streams处理完数据的topic
     */
    public static final String RAW_OUTPUT_TOPIC = "test-event-output-topic";
    public static final String OUTPUT_HOUR_TOPIC = "yzemeter-hour-topic";
    public static final String OUTPUT_DAY_TOPIC = "yzemeter-day-topic";
    public static final String OUTPUT_MONTH_TOPIC = "yzemeter-month-topic";
    public static final String OUTPUT_YEAR_TOPIC = "yzemeter-year-topic";

    private static final Properties props = new Properties();

    /**
     * Kafka Streams输出的所有topic
     */
    public static final List<String> topics = new ArrayList<>();

    static {
        // kafka consuemr的配置
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

        // Kafka Streams输出的所有topic
        topics.add(RAW_OUTPUT_TOPIC);
        topics.add(OUTPUT_HOUR_TOPIC); // 1
        topics.add(OUTPUT_DAY_TOPIC); // 2
        topics.add(OUTPUT_MONTH_TOPIC); // 3
        topics.add(OUTPUT_YEAR_TOPIC); // 4
    }

    public static void main(String[] args) throws SchedulerException {
        EntityDao entityDao = new EntityDao();
        EntityService service = new EntityService(entityDao);
        // 开启调度程序
        scheduleStart(service);

        // 启动kafka consumer
        KafkaConsumer<String, Entity> consumer = new KafkaConsumer<>(props);
        // 订阅输出topic
        consumer.subscribe(topics);
        while (true) {
            ConsumerRecords<String, Entity> data = consumer.poll(10);
            for (ConsumerRecord<String, Entity> record : data) {
                String key = record.key();
                Entity entity = record.value();
                System.out.printf("consumer get (key = %s, value = {uuid = %s, run_at = %d, value = %f} from topic : %s)\n", key, entity.getUuid(), entity.getRunAt(), entity.getValue(), record.topic());

                // 持久化到数据表中
                service.saveEmeterEntity(entity, record.topic());
            }
            // commit offset
            consumer.commitAsync();
        }
    }

    /**
     * 开启调度：定时将计量实体转换为管理实体
     */
    private static void scheduleStart(EntityService service) throws SchedulerException {
        // TODO 第一个版本只是关注现有的管理实体
        List<String> entityList = new ArrayList<>(4);
        entityList.add("S0004");
        entityList.add("S000420010000");
        entityList.add("S000420020000");
        entityList.add("S000420030000");

        JobDetail job = JobBuilder.newJob(AggregateManageEntityJob.class)
                .withIdentity(Constant.JOB_NAME, Constant.GROUP_NAME)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(Constant.TRIGGER_NAME, Constant.GROUP_NAME)
                .withSchedule(CronScheduleBuilder.cronSchedule(Constant.CRON))
                .build();

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        // 将service对象传递给job
        scheduler.getContext().put(Constant.ENTITY_SERVICE, service);
        scheduler.getContext().put(Constant.ENTITY_LIST, entityList);

        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }
}
