package com.smartgreen.quartz;

import java.util.Calendar;

/**
 * @Description 时间工具类
 * @Author Honda
 * @Date 2019/4/25 17:22
 **/
public class TimeUtil {

    /**
     * 得到整点调度时间戳
     * @param currTime 当前时间戳
     * @return 整点的时间戳
     */
    public static long getScheduleTime(long currTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currTime);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
}
