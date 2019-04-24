package com.smartgreen.common.utils.time;

import java.util.Calendar;

/**
 * @Description 时间相关工具类
 * @Author Honda
 * @Date 2019/4/24 19:01
 **/
public class TimeUtil {

    public static final int HOUR_MS = 3600000;
    public static final long DAY_MS = 86400000L;
    /**
     * 东八区转化为格林时间，需要减去这部分（16个小时）
     */
    public static final long DELTA = 576000000L;

    public static boolean isNewHour(long time) {
        return time % HOUR_MS == 0;
    }

    public static boolean isNewDay(long time) {
        return (time - DELTA) % DAY_MS == 0;
    }

    public static boolean isNewMonth(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.DAY_OF_MONTH) == 1 && isNewDay(time);
    }

    public static boolean isNewYear(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.MONTH) == 0 && isNewMonth(time);
    }

    /**
     * 得到时间段的开始时间点
     * 例如时间序列[a, b, c, d]，现在时间点为c，需要找到a的时间点（时间戳）
     * @param currTime 当前时间
     * @param type 分时类型
     * @return
     */
    public static long getOriginalTimestamp(long currTime, TimeType type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currTime);
        switch (type) {
            case YEAR:
                calendar.set(Calendar.MONTH, 0);
            case MONTH:
                calendar.set(Calendar.DAY_OF_MONTH, 1);
            case DAY:
                calendar.set(Calendar.HOUR, 0);
            case HOUR:
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
        }
        return calendar.getTimeInMillis();
    }

}
