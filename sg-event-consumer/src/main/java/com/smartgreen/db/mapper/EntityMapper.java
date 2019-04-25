package com.smartgreen.db.mapper;

import com.smartgreen.model.Entity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MyBatis映射器
 */
public interface EntityMapper {

    int insertOne(@Param("entity") Entity entity, @Param("tableName") String tableName);

    List<Entity> getListAt(@Param("timestamp") long timestamp, @Param("tableName") String tableName);

}
