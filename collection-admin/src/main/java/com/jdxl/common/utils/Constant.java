package com.jdxl.common.utils;

/**
 * 常量
 */
public class Constant {
	/** 超级管理员ID */
	public static final int SUPER_ADMIN = 1;

    /** 数据权限过滤 */
    public static final String SQL_FILTER = "sql_filter";

    public final static String COMMON_ESCAPE_STR = "\\";
    public final static String COMMA_SPLIT_STR = ",";
    public final static String COMMON_COLON_STR = ":";


    // redis
    public static String REDIS_DEFAULT_CHARSET = "UTF-8";
    public static String REDIS_DEFAULT_OK = "ok";
    public final static Integer REDIS_DEFAULT_RESULT = 1;
    public final static Long REDIS_DEFAULT_RESULT_LONG = 1L;

	/**
	 * 菜单类型
	 */
    public enum MenuType {
        /**
         * 目录
         */
    	CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 定时任务类型
     */
    public enum ScheduleType {
        /**
         * 多次
         */
        MULTIPLE(0),
        /**
         * 仅一次
         */
        ONETIME(1);

        private int value;

        ScheduleType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 定时任务状态
     */
    public enum ScheduleStatus {
        /**
         * 结束
         */
        FINISHED(-1),
        /**
         * 正常
         */
    	NORMAL(0),
        /**
         * 暂停
         */
    	PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }

    /**
     * 云服务商
     */
    public enum CloudService {
        /**
         * 七牛云
         */
        QINIU(1),
        /**
         * 阿里云
         */
        ALIYUN(2),
        /**
         * 腾讯云
         */
        QCLOUD(3);

        private int value;

        CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static final String SEPARATOR_LINE = System.getProperty("line.separator");
}
