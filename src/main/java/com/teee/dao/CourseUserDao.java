package com.teee.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teee.domain.course.CourseUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseUserDao extends BaseMapper<CourseUser> {
}
