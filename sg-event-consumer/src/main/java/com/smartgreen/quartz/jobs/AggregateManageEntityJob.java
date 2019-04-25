package com.smartgreen.quartz.jobs;

import com.smartgreen.db.table.Tables;
import com.smartgreen.model.Entity;
import com.smartgreen.quartz.TimeUtil;
import com.smartgreen.service.EntityService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

/**
 * @Description 定时将计量实体根据公式转化为管理实体
 * TODO 现在知识测试阶段，只是面向一个电表做的向管理实体的转换
 * @Author Honda
 * @Date 2019/4/25 11:44
 **/
public class AggregateManageEntityJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 得到传递的对象
        EntityService service = (EntityService)context.get(Constant.ENTITY_SERVICE);
        List<String> list = (List<String>) context.get(Constant.ENTITY_LIST);

        // i为timeType，[1,4]代表hour，day，month，year
        for (int i = 1; i <= 4; i++) {
            for (String uuid : list) {
                // 得到调度批次时间
                long scheduleTime = TimeUtil.getScheduleTime(System.currentTimeMillis());
                // 获取分时好的计量实体表
                String sourceTable = Tables.getTimeTableName(Tables.EMETER_TYPE, i);
                List<Entity> listAt = service.getListAt(scheduleTime, sourceTable);
                // TODO 当前版本只关注一个电表，只需要将uuid简单的置换就行，未来需要根据公式来计算
                Entity data = listAt.get(0);
                data.setUuid(uuid);

                // 从计量实体表中获取数据
                int entityType = Tables.getEntityType(uuid);
                String toTable = Tables.getTimeTableName(entityType, i);
                service.saveEntity(data, toTable);
            }
        }
    }
}
