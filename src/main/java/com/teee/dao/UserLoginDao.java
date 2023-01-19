package com.teee.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teee.domain.user.UserLogin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserLoginDao extends BaseMapper<UserLogin> {
}
