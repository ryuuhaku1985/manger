package com.jdxl.modules.app.message;


import com.jdxl.common.message.MsgInfo;

/**
 * SYS模块错误码
 */
public enum AppMsgInfo implements MsgInfo {

    USR0001("USR0001", "手机号或密码错误"),
    USR0002("USR0002", "验证码已失效"),
    USR0003("USR0003", "{}失效，请重新登录"),

    /** 菜单名称不能为空 */
    SYS9999("APP9999", "{}");

    private String code;
    private String message;

    AppMsgInfo(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String toString() {
        return "[" + this.code + "]" + this.message;
    }
}
