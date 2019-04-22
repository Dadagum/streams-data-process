package com.smartgreen.mybatis.mapper;

import com.smartgreen.model.Entity;
import org.apache.ibatis.annotations.Param;

/**
 * MyBatis映射器
 */
public interface EntityMapper {

    int insertOne(@Param("entity") Entity entity, @Param("tableName") String tableName);
}
