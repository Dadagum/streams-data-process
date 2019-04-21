package com.smartgreen;

import com.smartgreen.common.Constant;
import com.smartgreen.common.ProcessorSuppliers;
import com.smartgreen.common.SerdesUtils;
import com.smartgreen.processor.InterpolationProcessor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.state.Stores;

import java.util.Properties;

public class EngineRunner {

    private static final Properties props = new Properties();

    static {
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, Constant.APPLICATION);
        props.put(StreamsConfig.CLIENT_ID_CONFIG, Constant.CLIENT_ID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Constant.SERVER);
        props.put("schema.registry.url", Constant.SCHEMA_URL);
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.AT_LEAST_ONCE);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
    }

    public static void main(String[] args) {
        Topology builder = new Topology();
        // 增加source processor
        builder.addSource("Source", new StringDeserializer(), SerdesUtils.createEventDeserializer(), Constant.INPUT_TOPIC);
        // 建立拓扑结构
        // 插值处理
        builder.addProcessor(InterpolationProcessor.NAME, new ProcessorSuppliers.InterpolationProcessorSupplier(), "Source");
        // 调试阶段使用inMemoryKeyValueStore
        builder.addStateStore(Stores.keyValueStoreBuilder(Stores.inMemoryKeyValueStore(InterpolationProcessor.DATASTORE), Serdes.String(), SerdesUtils.createEventSerde()), InterpolationProcessor.NAME);
        builder.addSink("Sink", Constant.OUTPUT_TOPIC, new StringSerializer(), SerdesUtils.createEventSerializer(), InterpolationProcessor.NAME);

        // 根据已经创建完的拓扑结构和配置开启streams程序
        final KafkaStreams streams = new KafkaStreams(builder, props);
        // 清除之前的状态
        // from : https://stackoverflow.com/questions/46681844/kafka-streams-remove-clear-state-store
        streams.cleanUp();
        // Start the Kafka Streams threads
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
