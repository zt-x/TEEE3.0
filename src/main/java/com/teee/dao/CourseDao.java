package com.teee.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teee.domain.course.Course;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseDao extends BaseMapper<Course> {
}
