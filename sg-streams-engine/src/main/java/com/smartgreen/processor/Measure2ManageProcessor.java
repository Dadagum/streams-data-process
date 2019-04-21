package com.smartgreen.processor;

import com.micer.core.event.Event.Event;
import com.smartgreen.model.ManageEntity;
import com.smartgreen.model.ManageEntityFormula;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计量实体转换为管理实体的Processor
 * 现在为测试时期，只有一个设备
 */
public class Measure2ManageProcessor implements Processor<String, Event> {

    public static final String NAME = "measure2manage-processor";

    private ProcessorContext context; // 可以的得到流处理的上下文，init()函数中必须赋值

    private String processorName;

    public Measure2ManageProcessor() {
    }

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public void process(String s, Event curr) {
        ManageEntityFormula formula = getFormula();
        List<ManageEntity> entities = formula.getFormula();
        for (ManageEntity entity : entities) {
            Event event = new Event();
            event.setEventId(curr.getEventId());
            event.setValues(curr.getValues());
            event.setTimestamp(curr.getTimestamp());
            event.setDeviceProtocolId(curr.getDeviceProtocolId());
            event.setDeviceConfigId(entity.getUuid());

            context.forward(s, event);
        }
        context.commit();
    }

    @Override
    public void close() {

    }

    /**
     * 测试阶段，暂时将映射关系写死到代码中
     * @return
     */
    private ManageEntityFormula getFormula() {
        List<ManageEntity> list = new ArrayList<>(4);
        Map<String, Double> compose = new HashMap<>();
        compose.put("YZ001", 1d);
        ManageEntity project = new ManageEntity("ZHJZ001", compose);
        ManageEntity building = new ManageEntity("SCUTb5", compose);
        ManageEntity floor = new ManageEntity("SCUTb5_f9", compose);
        ManageEntity room = new ManageEntity("SCUTb5_902", compose);
        list.add(project);
        list.add(building);
        list.add(floor);
        list.add(room);

        ManageEntityFormula formula = new ManageEntityFormula(list);
        return formula;
    }

}
