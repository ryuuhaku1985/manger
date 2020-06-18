package com.jdxl.modules.sys.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jdxl.datasources.annotation.DataSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.jdxl.common.utils.PageUtils;
import com.jdxl.common.utils.Query;

import com.jdxl.modules.sys.dao.UserAccountDao;
import com.jdxl.modules.sys.entity.UserAccountEntity;
import com.jdxl.modules.sys.service.UserAccountService;
import org.springframework.transaction.annotation.Transactional;

@Service("userAccountService")
public class UserAccountServiceImpl extends ServiceImpl<UserAccountDao, UserAccountEntity> implements UserAccountService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        String mchId = (String)params.get("mchId");

        Page<UserAccountEntity> page = this.selectPage(
                new Query<UserAccountEntity>(params).getPage(),
                new EntityWrapper<UserAccountEntity>()
                .eq(StringUtils.isNotBlank(mchId),"mch_Id", mchId)
                .eq("user_account_status", 2)
        );

        return new PageUtils(page);
    }

    // 向管理库写入
    @DataSource("manage")
    @Transactional(rollbackFor = Exception.class)
    public int addUserAccountToManage(UserAccountEntity entity) {
        return baseMapper.insert(entity);
    }

    @DataSource("master")
    public int selectCountFromManege(Wrapper<UserAccountEntity> wrapper) {
        return baseMapper.selectCount(wrapper);
    }

}
