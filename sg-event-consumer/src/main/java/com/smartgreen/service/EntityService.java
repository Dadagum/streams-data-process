package com.smartgreen.service;

import com.micer.core.event.Event.Event;
import com.smartgreen.model.Entity;
import com.smartgreen.mybatis.mapper.EntityMapper;
import com.smartgreen.mybatis.mybatis.MyBatisUtil;
import org.apache.avro.util.Utf8;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

public class EntityService {

    /**
     * 测试阶段，只有几个实体，直接在这里记录分时后的实体和数据表名的关系
     * key   : uuid
     * value : tableName
     */
    private static final Map<String, String> mapping = new HashMap<>();

    static {
        mapping.put("ZHJZ001", "d_project_15min_t");
        mapping.put("SCUTb5", "d_building_15min_t");
        mapping.put("SCUTb5_f9", "d_floor_15min_t");
        mapping.put("SCUTb5_902", "d_room_15min_t");
        mapping.put("YZ001", "d_emeter_raw_t");
    }

    public void saveEntity(Entity entity) {
        SqlSession session = MyBatisUtil.getSession();
        try {
            EntityMapper mapper = session.getMapper(EntityMapper.class);
            String tableName = mapping.get(entity.getUuid());
            mapper.insertOne(entity, tableName);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void saveEntity(Event event) {
        Entity entity = new Entity();
        entity.setAnomaly(false);
        entity.setOriginal(true);
        entity.setRunAt(event.getTimestamp());
        entity.setUuid(event.getDeviceConfigId().toString());

        Utf8 u = new Utf8("000");
        int value = Integer.parseInt(event.getValues().get(u).toString());
        // 实际度数为0.1
        entity.setValue(value * 0.1);

        saveEntity(entity);

    }
}
