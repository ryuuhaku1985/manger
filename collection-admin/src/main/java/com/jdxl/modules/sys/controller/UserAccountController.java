package com.jdxl.modules.sys.controller;

import java.util.Arrays;
import java.util.Map;

import com.jdxl.common.result.Result;
import com.jdxl.common.utils.MapUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jdxl.modules.sys.entity.UserAccountEntity;
import com.jdxl.modules.sys.service.UserAccountService;
import com.jdxl.common.utils.PageUtils;


/**
 * 用户账户表
 *
 * @author CodeGen
 * @date 2018-09-21 15:24:19
 */
@RestController
@RequestMapping("sys/useraccount")
public class UserAccountController {
    @Autowired
    private UserAccountService userAccountService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:useraccount:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = userAccountService.queryPage(params);

        return Result.response(new MapUtils().put("page", page));
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:useraccount:info")
    public Result info(@PathVariable("id") Long id){
			UserAccountEntity userAccount = userAccountService.selectById(id);

        return Result.response(new MapUtils().put("userAccount", userAccount));
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:useraccount:save")
    public Result save(@RequestBody UserAccountEntity userAccount){
			userAccountService.insert(userAccount);

        return Result.response();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:useraccount:update")
    public Result update(@RequestBody UserAccountEntity userAccount){
			userAccountService.updateById(userAccount);

        return Result.response();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:useraccount:delete")
    public Result delete(@RequestBody Long[] ids){
			userAccountService.deleteBatchIds(Arrays.asList(ids));

        return Result.response();
    }

}
