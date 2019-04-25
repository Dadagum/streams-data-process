package com.smartgreen.db;

import com.smartgreen.model.Entity;
import com.smartgreen.db.mapper.EntityMapper;
import com.smartgreen.db.utils.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * @Description 封装mapper
 * @Author Honda
 * @Date 2019/4/25 14:35
 **/
public class EntityDao {

    /**
     * 主要针对消费者
     * @param entity 需要记录的实体数据
     * @param tableName 需要插入的数据表名称
     */
    public void saveEntity(Entity entity, String tableName) {
        SqlSession session = MyBatisUtil.getSession();
        try {
            EntityMapper mapper = session.getMapper(EntityMapper.class);
            mapper.insertOne(entity, tableName);
            session.commit();
        } finally {
            session.close();
        }
    }

    public List<Entity> getListAt(long timestamp, String tableName) {
        SqlSession session = MyBatisUtil.getSession();
        List<Entity> result = null;
        try {
            EntityMapper mapper = session.getMapper(EntityMapper.class);
            result = mapper.getListAt(timestamp, tableName);
        } finally {
            session.close();
        }
        return result;
    }
}
