package com.smartgreen.mybatis.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * 官方文档：
 * SqlSessionFactoryBuilder：这个类可以被实例化、使用和丢弃，一旦创建了 SqlSessionFactory，就不再需要它了
 * SqlSessionFactory： 一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或重新创建另一个实例
 * SqlSession：每个线程都应该有它自己的 SqlSession 实例，SqlSession 的实例不是线程安全的
 */
public class MyBatisUtil {

    private static final String CONFIG_PATH = "mybatis/mybatis-cfg.xml";

    private static SqlSessionFactory sessionFactory;

    private MyBatisUtil() {
    }

    /**
     * 初始化SqlSessionFactory
     */
    static {
        try {
            InputStream is = Resources.getResourceAsStream(CONFIG_PATH);
            sessionFactory = new SqlSessionFactoryBuilder().build(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSessionFactory getFactory() {
        return sessionFactory;
    }

    public static SqlSession getSession() {
        return sessionFactory.openSession();
    }


}
