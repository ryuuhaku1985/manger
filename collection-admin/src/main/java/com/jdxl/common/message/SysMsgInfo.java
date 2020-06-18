package com.jdxl.common.message;

/**
 * SYS模块错误码
 */
public enum SysMsgInfo implements MsgInfo {

    SYS0001("SYS0001", "{0}不能为空"),
    SYS0002("SYS0002", "上级菜单只能为{0}类型"),
    SYS0003("SYS0003", "新增用户所选角色，不是本人创建"),
    SYS0004("SYS0004", "新增角色的权限，已超出你的权限范围"),
    SYS0005("SYS0005", "获取参数失败"),
    SYS0006("SYS0006", "uuid不能为空"),
    SYS0007("SYS0007", "生成Token失败"),
//    SYS0008("SYS0008", "Redis服务异常"),
    SYS0009("SYS0009", "包含非法字符"),
    SYS0010("SYS0010", "验证码已失效"),
    SYS0011("SYS0011", "新密码不为能空"),
    SYS0012("SYS0012", "原密码不正确"),
    SYS0013("SYS0013", "系统管理员不能删除"),
    SYS0014("SYS0014", "当前用户不能删除"),
    SYS0015("SYS0015", "系统菜单不能删除"),
    SYS0016("SYS0016", "请先删除子菜单或按钮"),
    SYS0017("SYS0017", "验证码不正确"),
    SYS0018("SYS0018", "账号或密码不正确"),
    SYS0019("SYS0019", "The account has been locked, please contact the administrator"),
    SYS0020("SYS0020", "获取参数失败"),
    SYS0021("SYS0021", "数据权限接口，只能是Map类型参数，且不能为NULL"),
    SYS0022("SYS0022", "请先删除子机构"),
    SYS0023("SYS0023", "操作失败"),
    SYS0024("SYS0024", "Failed to send validation code. Please try again."),
    SYS0025("SYS0025", "The verification code entered is incorrect. Please re-enter."),
    SYS0026("SYS0026", "The verification code entered is empty. Please enter the verification code"),
    SYS0027("SYS0027", "Input mobile phone number and registered mobile phone number is not consistent, please re-enter"),
    SYS0028("SYS0028", "The verification code has expired. Please re-initiate verification"),

    /** 自定义异常 */
    SYS9999("SYS9999", "{0}"),

    ACTIVITY_NOT_EXIST("000027", "活动不存在"),
    ACTIVITY_STATU_ERROR("3051", "该活动处于不可用状态"),

    OSS0001("OSS0001", "上传文件不能为空"),
    OSS0002("OSS0002", "上传出错：{0}"),
    OSS0003("OSS0003", "上传文件失败，请核对配置信息"),
    OSS0004("OSS0004", "上传文件失败"),

    /** {}定时任务失败 */
    JOB0001("JOB0001", "{0}定时任务失败"),
    /** 获取定时任务CronTrigger出现异常 */
    JOB0002("JOB0002", "获取定时任务CronTrigger出现异常"),
    JOB0003("JOB0003", "当前参数获取数据失败,请检查传入参数是否正确"),
    JOB0004("JOB0004", "当前参数无需改变,请仔细确认");

    private String code;
    private String message;

    private SysMsgInfo(String code, String message) {
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
