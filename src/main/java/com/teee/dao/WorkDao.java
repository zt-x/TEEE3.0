package com.teee.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teee.domain.work.Work;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkDao extends BaseMapper<Work> {
}
