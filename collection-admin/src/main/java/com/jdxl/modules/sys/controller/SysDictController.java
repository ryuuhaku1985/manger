package com.jdxl.modules.sys.controller;

import com.jdxl.common.annotation.SysLog;
import com.jdxl.common.result.Result;
import com.jdxl.common.utils.MapUtils;
import com.jdxl.common.utils.PageUtils;
import com.jdxl.common.validator.ValidatorUtils;
import com.jdxl.modules.sys.entity.SysDictEntity;
import com.jdxl.modules.sys.service.SysDictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("sys/dict")
public class SysDictController extends AbstractController {

    @Autowired
    private SysDictService sysDictService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:dict:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = sysDictService.queryPage(params);
        return Result.response(new MapUtils().put("page", page));
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:dict:info")
    public Result info(@PathVariable("id") Long id){
        SysDictEntity dict = sysDictService.selectById(id);

        return Result.response(new MapUtils().put("dict", dict));
    }

    /**
     * 保存
     */
    @SysLog("保存数据字典")
    @RequestMapping("/save")
    @RequiresPermissions("sys:dict:save")
    public Result save(@RequestBody SysDictEntity dict){
        //校验类型
        ValidatorUtils.validateEntity(dict);

        sysDictService.insert(dict);

        return Result.response();
    }

    /**
     * 修改
     */
    @SysLog("修改数据字典")
    @RequestMapping("/update")
    @RequiresPermissions("sys:dict:update")
    public Result update(@RequestBody SysDictEntity dict){
        //校验类型
        ValidatorUtils.validateEntity(dict);

        sysDictService.updateById(dict);

        return Result.response();
    }

    /**
     * 删除
     */
    @SysLog("删除数据字典")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:dict:delete")
    public Result delete(@RequestBody Long[] ids){
        sysDictService.deleteBatchIds(Arrays.asList(ids));

        return Result.response();
    }
}
