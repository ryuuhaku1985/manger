package com.jdxl.modules.sys.form;

import lombok.Data;

/**
 * 登录表单
 */
@Data
public class SysLoginForm {

    private String username;
    private String password;
    private String captcha;
    private String uuid;
    private String mobile;
    private String verifycode;
}
