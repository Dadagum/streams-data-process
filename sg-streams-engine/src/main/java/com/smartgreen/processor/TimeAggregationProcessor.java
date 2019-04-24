package com.smartgreen.processor;

import com.smartgreen.common.constant.Constant;
import com.smartgreen.common.utils.time.TimeType;
import com.smartgreen.common.utils.time.TimeUtil;
import com.smartgreen.model.Entity;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

/**
 * @Description 分时聚合的Processor
 * key：uuid
 * value：计量实体Entity
 * @Author Honda
 * @Date 2019/4/24 17:24
 **/
public class TimeAggregationProcessor implements Processor<String, Entity> {

    public static final String NAME = "time-aggregation-processor";

    public static final String HOUR_DS = "hourDataStore";
    public static final String DAY_DS = "dayDataStore";
    public static final String MONTH_DS = "monthDataStore";
    public static final String YEAR_DS = "yearDataStore";

    private ProcessorContext context; // 可以的得到流处理的上下文，init()函数中必须赋值

    private KeyValueStore<String, Entity> hourDataStore;
    private KeyValueStore<String, Entity> dayDataStore;
    private KeyValueStore<String, Entity> monthDataStore;
    private KeyValueStore<String, Entity> yearDataStore;

    @Override
    public void init(ProcessorContext processorContext) {
        this.context = context;
        hourDataStore = (KeyValueStore<String, Entity>) context.getStateStore(HOUR_DS);
        dayDataStore = (KeyValueStore<String, Entity>) context.getStateStore(DAY_DS);
        monthDataStore = (KeyValueStore<String, Entity>) context.getStateStore(MONTH_DS);
        yearDataStore = (KeyValueStore<String, Entity>) context.getStateStore(YEAR_DS);
    }

    @Override
    public void process(String s, Entity entity) {

    }

    /**
     * TODO 需要确定业务规则
     * 分时聚合
     */
    private void hour(Entity curr) {
        Entity pre = hourDataStore.get(curr.getUuid().toString());
        double value = 0d; // 记录能耗
        if (pre != null) {
            value = curr.getValue() - pre.getValue();
        } else {
            long preRunAt = TimeUtil.getOriginalTimestamp(curr.getRunAt(), TimeType.DAY);
            pre = Entity.newBuilder()
                    .setUuid(curr.getUuid())
                    .setValue(curr.getValue())
                    .setRunAt(preRunAt)
                    .setId(0)
                    .setAnomaly(curr.getAnomaly())
                    .setOriginal(curr.getOriginal())
                    .build();
        }
        // 检查是否为整时
        if (!TimeUtil.isNewDay(curr.getRunAt())) {
            // 保留原来的一切信息（时间戳，值，是否插值，业务错误）
            curr.setRunAt(pre.getRunAt());
            curr.setValue(pre.getValue());
            curr.setAnomaly(pre.getAnomaly());
            curr.setOriginal(pre.getOriginal());
        }
        hourDataStore.put(curr.getUuid().toString(), curr);
        // 记录更新能耗值
        pre.setValue(value);
        // 传给下游的相对应的processor
        context.forward(pre.getUuid().toString(), pre, Constant.SINK_HOUR_PROCESSOR);
    }


    private void day(Entity entity) {

    }

    private void month(Entity entity) {

    }

    private void year(Entity entity) {

    }


    @Override
    public void close() {
    }


}
