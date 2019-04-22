package com.smartgreen.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 实现Druid数据源的创建工厂
 */
public class DruidDataSourceFactory implements DataSourceFactory {

    private Properties cfg;

    @Override
    public void setProperties(Properties properties) {
        cfg = properties;
    }

    @Override
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(cfg.getProperty("driver"));
        dataSource.setUrl(cfg.getProperty("url"));
        dataSource.setUsername(cfg.getProperty("username"));
        dataSource.setPassword(cfg.getProperty("password"));
        try {
            dataSource.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }
}
