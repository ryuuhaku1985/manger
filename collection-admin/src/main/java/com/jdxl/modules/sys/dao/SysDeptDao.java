package com.jdxl.modules.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jdxl.modules.sys.entity.SysDeptEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 机构管理
 */
@Repository
@Mapper
public interface SysDeptDao extends BaseMapper<SysDeptEntity> {

    /**
     * 查询子机构ID列表
     * @param parentId  上级机构ID
     */
    List<Long> queryDetpIdList(Long parentId);

}
