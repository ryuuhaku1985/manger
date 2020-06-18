package com.jdxl.modules.utils.enums;

public enum ExceptionWarnTypeEnum {
    /************** 映射表：异常类型 -> 角色 *************/
    PROGRAM_BUG("程序bug", "DEVELOPER"), // 程序bug
    DATA_EXCEPTION("数据异常", "LEADER, DEVELOPER"), // 数据异常
    I_O_EXCEPTION("I/O 异常", "LEADER, OPERATER, DEVELOPER"), // io 异常
    OTHER_EXCEPTION("其他异常", "LEADER, DEVELOPER"), // 其他异常
    REMIND_MESSAGE("提示信息", "LEADER, DEVELOPER"); // 提示信息

    private String message;
    private String exceptionWarnAcceptRoles;

    private ExceptionWarnTypeEnum(String message, String exceptionWarnAcceptRole) {
        this.message = message;
        this.exceptionWarnAcceptRoles = exceptionWarnAcceptRole;
    }

    public String getExceptionWarnAcceptRoles() {
        return exceptionWarnAcceptRoles;
    }

    public String getMessage() {
        return message;
    }
}
