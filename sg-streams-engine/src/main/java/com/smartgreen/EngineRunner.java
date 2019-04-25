package com.smartgreen;

import com.smartgreen.common.constant.Constant;
import com.smartgreen.common.serdes.EntitySerdes;
import com.smartgreen.common.suppliers.ProcessorSuppliers;
import com.smartgreen.common.serdes.EventSerdes;
import com.smartgreen.model.Entity;
import com.smartgreen.processor.InterpolationProcessor;
import com.smartgreen.processor.TimeAggregationProcessor;
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
        // 建立拓扑结构
        Topology builder = new Topology();
        // SOURCE PROCESSOR
        builder.addSource(Constant.SOURCE_PROCESSOR, new StringDeserializer(), EventSerdes.deserializer(), Constant.INPUT_TOPIC);
        // 插值处理
        builder.addProcessor(InterpolationProcessor.NAME, new ProcessorSuppliers.InterpolationProcessorSupplier(), Constant.SOURCE_PROCESSOR);
        builder.addStateStore(Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(InterpolationProcessor.DATASTORE), Serdes.String(), EntitySerdes.serde()), InterpolationProcessor.NAME);
        // 分时处理
        builder.addProcessor(TimeAggregationProcessor.NAME, new ProcessorSuppliers.TimeAggregationProcessorSupplier(), InterpolationProcessor.NAME);
        builder.addStateStore(Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(TimeAggregationProcessor.HOUR_DS), Serdes.String(), EntitySerdes.serde()), TimeAggregationProcessor.NAME);
        builder.addStateStore(Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(TimeAggregationProcessor.DAY_DS), Serdes.String(), EntitySerdes.serde()), TimeAggregationProcessor.NAME);
        builder.addStateStore(Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(TimeAggregationProcessor.MONTH_DS), Serdes.String(), EntitySerdes.serde()), TimeAggregationProcessor.NAME);
        builder.addStateStore(Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(TimeAggregationProcessor.YEAR_DS), Serdes.String(), EntitySerdes.serde()), TimeAggregationProcessor.NAME);
        // SINK PROCESSOR
        // 记录插值完的数据
        builder.addSink(Constant.SINK_RAW_PROCESSOR, Constant.RAW_OUTPUT_TOPIC, new StringSerializer(), EventSerdes.serializer(), InterpolationProcessor.NAME);
        // 分时数据
        builder.addSink(Constant.SINK_HOUR_PROCESSOR, Constant.OUTPUT_HOUR_TOPIC, new StringSerializer(), EventSerdes.serializer(), TimeAggregationProcessor.NAME);
        builder.addSink(Constant.SINK_DAY_PROCESSOR, Constant.OUTPUT_DAY_TOPIC, new StringSerializer(), EventSerdes.serializer(), TimeAggregationProcessor.NAME);
        builder.addSink(Constant.SINK_MONTH_PROCESSOR, Constant.OUTPUT_MONTH_TOPIC, new StringSerializer(), EventSerdes.serializer(), TimeAggregationProcessor.NAME);
        builder.addSink(Constant.SINK_YEAR_PROCESSOR, Constant.OUTPUT_YEAR_TOPIC, new StringSerializer(), EventSerdes.serializer(), TimeAggregationProcessor.NAME);

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
