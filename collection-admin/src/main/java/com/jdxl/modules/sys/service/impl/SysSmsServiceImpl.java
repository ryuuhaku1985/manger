package com.jdxl.modules.sys.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.jdxl.common.utils.PageUtils;
import com.jdxl.common.utils.Query;

import com.jdxl.modules.sys.dao.SysSmsDao;
import com.jdxl.modules.sys.entity.SysSmsEntity;
import com.jdxl.modules.sys.service.SysSmsService;


@Service("sysSmsService")
public class SysSmsServiceImpl extends ServiceImpl<SysSmsDao, SysSmsEntity> implements SysSmsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<SysSmsEntity> page = this.selectPage(
                new Query<SysSmsEntity>(params).getPage(),
                new EntityWrapper<SysSmsEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public SysSmsEntity selectbymobile(Map<String, Object> params) {
        return  this.baseMapper.selectbymobile(params);
    }

}
