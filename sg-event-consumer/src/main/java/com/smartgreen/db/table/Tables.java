package com.smartgreen.db.table;

import com.smartgreen.ConsumerRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 数据库表相关
 * @Author Honda
 * @Date 2019/4/25 15:46
 **/
public class Tables {

    public static final List<String> list = new ArrayList<>(21);

    /**
     * 为了拼接得到相应的数据表名称
     * 数据表名： d_${eneity_type}_${time_type}_t
     */
    public static final int EMETER_TYPE = 1;
    public static final int ROOM_TYPE = 2;
    public static final int FLOOR_TYPE = 3;
    public static final int BUILDING_TYPE = 4;
    public static final int PROJECT_TYPE = 5;

    static {
        // 原始记录表
        list.add("d_emeter_raw_t");
        // 电表分时记录表
        list.add("d_emeter_hour_t");
        list.add("d_emeter_day_t");
        list.add("d_emeter_month_t");
        list.add("d_emeter_year_t");
        // 房间分时记录
        list.add("d_room_hour_t");
        list.add("d_room_day_t");
        list.add("d_room_month_t");
        list.add("d_room_year_t");
        // 楼层分时记录
        list.add("d_floor_hour_t");
        list.add("d_floor_day_t");
        list.add("d_floor_month_t");
        list.add("d_floor_year_t");
        // 建筑分时记录
        list.add("d_building_hour_t");
        list.add("d_building_day_t");
        list.add("d_building_month_t");
        list.add("d_building_year_t");
        // 项目分时记录
        list.add("d_project_hour_t");
        list.add("d_project_day_t");
        list.add("d_project_month_t");
        list.add("d_project_year_t");
    }

    /**
     * 面向消费者程序的门面
     * @param topicName 数据来源topic名称
     * @return 需要插入的数据表名称
     */
    public static String getTableName(String topicName) {
        if (topicName.equals(ConsumerRunner.RAW_OUTPUT_TOPIC)) {
            return list.get(0);
        }
        int timeType = getTimeType(topicName);
        return getTimeTableName(EMETER_TYPE, timeType);
    }

    /**
     * 范围[1,4]
     * 根据输出的topicName转化为分时的类型
     * @param topicName kafka 的topic名称
     * @return 分时的维度
     * 注意：如果不是分时的数据（即来自RAW_OUTPUT_TOPIC的数据），那么返回的值为0
     */
    public static int getTimeType(String topicName) {
        List<String> topics = ConsumerRunner.topics;
        for (int i = 0; i < topics.size(); i++) {
            if (topicName.equals(topics.get(i))) {
                return i;
            }
        }
        throw new RuntimeException();
    }

    /**
     * 范围[1, 5]
     * 根据编码规则获得实体的类型
     * @param uuid 实体的uuid
     * @return 实体的类型
     */
    public static int getEntityType(String uuid) {
        if (uuid.length() == 5) {
            return PROJECT_TYPE;
        } else if (uuid.length() == 13) {
            if (uuid.charAt(5) == '1') {
                return EMETER_TYPE;
            } else if (uuid.charAt(6) == '2') {
                if (uuid.charAt(8) == '1') {
                    return BUILDING_TYPE;
                } else if (uuid.charAt(8) == '2') {
                    return FLOOR_TYPE;
                } else if (uuid.charAt(8) == '3') {
                    return ROOM_TYPE;
                }
            }
        }
        throw new RuntimeException("uuid编码错误");
    }

    /**
     * 需要得到分时表才需要调用该函数
     * 根据实体的类型和分时的维度得到数据表的名称
     * @param entityType 实体的类型
     * @param timeType 分时的维度
     * @return 对应的数据表的名称
     */
    public static String getTimeTableName(int entityType, int timeType) {
        int index = 4 * (entityType - 1) + timeType;
        return list.get(index);
    }
}
