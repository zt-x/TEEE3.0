package com.teee.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teee.domain.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoDao extends BaseMapper<UserInfo> {
}
