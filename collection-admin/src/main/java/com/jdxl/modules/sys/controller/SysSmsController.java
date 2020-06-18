package com.jdxl.modules.sys.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jdxl.modules.sys.entity.SysSmsEntity;
import com.jdxl.modules.sys.service.SysSmsService;
import com.jdxl.common.utils.PageUtils;
import com.jdxl.common.utils.MapUtils;
import com.jdxl.common.result.Result;

/**
 * 
 *
 * @author CodeGen
 * @date 2019-08-14 16:42:21
 */
@RestController
@RequestMapping("sys/syssms")
public class SysSmsController {
    @Autowired
    private SysSmsService sysSmsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:syssms:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = sysSmsService.queryPage(params);

        return Result.response(new MapUtils().put("page", page));
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:syssms:info")
    public Result info(@PathVariable("id") Integer id){
			SysSmsEntity sysSms = sysSmsService.selectById(id);

        return Result.response(new MapUtils().put("sysSms", sysSms));
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:syssms:save")
    public Result save(@RequestBody SysSmsEntity sysSms){
			sysSmsService.insert(sysSms);

        return Result.response();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:syssms:update")
    public Result update(@RequestBody SysSmsEntity sysSms){
			sysSmsService.updateById(sysSms);

        return Result.response();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:syssms:delete")
    public Result delete(@RequestBody Integer[] ids){
			sysSmsService.deleteBatchIds(Arrays.asList(ids));

        return Result.response();
    }

}
