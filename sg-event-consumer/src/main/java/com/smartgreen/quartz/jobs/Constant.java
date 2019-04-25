package com.smartgreen.quartz.jobs;

/**
 * @Description 调度相关的常量
 * @Author Honda
 * @Date 2019/4/25 14:52
 **/
public class Constant {

    /**
     * cron表达式
     */
    public static final String CRON = "0/5 * * * * ?";

    /**
     * 传递给job的参数
     */
    public static final String ENTITY_SERVICE = "entityService";
    public static final String ENTITY_LIST = "entityList";

    /**
     * job，trigger组名
     */
    public static final String JOB_NAME = "aggJob";
    public static final String TRIGGER_NAME = "aggTrigger";
    public static final String GROUP_NAME = "agg-group";


}
