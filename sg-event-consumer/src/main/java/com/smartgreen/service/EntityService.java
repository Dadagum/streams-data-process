package com.smartgreen.service;

import com.micer.core.event.Event;
import com.smartgreen.ConsumerRunner;
import com.smartgreen.model.Entity;
import com.smartgreen.mybatis.mapper.EntityMapper;
import com.smartgreen.mybatis.mybatis.MyBatisUtil;
import org.apache.avro.util.Utf8;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

public class EntityService {

    /**
     * 记录topic对应数据库tableName
     */
    private static final Map<String, String> mapping = new HashMap<>();

    static {
        mapping.put(ConsumerRunner.OUTPUT_HOUR_TOPIC, "d_emeter_hour_t");
        mapping.put(ConsumerRunner.OUTPUT_DAY_TOPIC, "d_emeter_day_t");
        mapping.put(ConsumerRunner.OUTPUT_MONTH_TOPIC, "d_emeter_month_t");
        mapping.put(ConsumerRunner.OUTPUT_YEAR_TOPIC, "d_emeter_year_t");
        mapping.put(ConsumerRunner.RAW_OUTPUT_TOPIC, "d_emeter_raw_t");
    }

    public void saveEntity(Entity entity, String topicName) {
        SqlSession session = MyBatisUtil.getSession();
        try {
            EntityMapper mapper = session.getMapper(EntityMapper.class);
            String tableName = mapping.get(topicName);
            mapper.insertOne(entity, tableName);
            session.commit();
        } finally {
            session.close();
        }
    }

    @Deprecated
    public void saveEntity(Event event) {

    }
}
