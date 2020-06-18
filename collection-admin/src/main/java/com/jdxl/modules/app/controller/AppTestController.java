package com.jdxl.modules.app.controller;

import com.jdxl.common.result.Result;
import com.jdxl.common.utils.MapUtils;
import com.jdxl.modules.app.annotation.LoginUser;
import com.jdxl.modules.app.annotation.Login;
import com.jdxl.modules.app.entity.UserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * APP测试接口
 */
@RestController
@RequestMapping("/app")
@Api("APP测试接口")
public class AppTestController {

    @Login
    @GetMapping("userInfo")
    @ApiOperation("获取用户信息")
    public Result userInfo(@LoginUser UserEntity user){
        return Result.response(new MapUtils().put("user", user));
    }

    @Login
    @GetMapping("userId")
    @ApiOperation("获取用户ID")
    public Result userInfo(@RequestAttribute("userId") Integer userId){
        return Result.response(new MapUtils().put("userId", userId));
    }

    @GetMapping("notToken")
    @ApiOperation("忽略Token验证测试")
    public Result notToken(){
        return Result.response(new MapUtils().put("msg", "无需token也能访问。。。"));
    }

}
