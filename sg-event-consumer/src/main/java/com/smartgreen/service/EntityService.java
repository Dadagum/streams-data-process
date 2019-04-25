package com.smartgreen.service;

import com.smartgreen.model.Entity;
import com.smartgreen.db.EntityDao;
import com.smartgreen.db.table.Tables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 实体相关的业务处理
 * @Author Honda
 * @Date 2019/4/25 15:08
 **/
public class EntityService {

    /**
     * 记录topic到数据表
     */
    private static final Map<String, Integer> map = new HashMap<>(5);

    static {

    }

    private EntityDao entityDao;

    public EntityService(EntityDao entityDao) {
        this.entityDao = entityDao;
    }

    /**
     * 将计量实体记录在数据表中
     * @param entity 计量实体数据
     * @param topicName 数据来自的topic
     */
    public void saveEmeterEntity(Entity entity, String topicName) {
        String tableName = Tables.getTableName(topicName);
        saveEntity(entity, tableName);
    }

    /**
     * 将实体保存在数据表中
     */
    public void saveEntity(Entity entity, String tableName) {
        entityDao.saveEntity(entity, tableName);
    }

    /**
     * 在数据表中获取某一批次的实体数据
     */
    public List<Entity> getListAt(long timestamp, String tableName) {
        return entityDao.getListAt(timestamp, tableName);
    }

}
