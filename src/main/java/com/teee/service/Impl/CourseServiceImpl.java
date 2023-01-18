package com.teee.service.Impl;

import com.teee.domain.course.Course;
import com.teee.service.CourseService;
import com.teee.vo.Result;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {
    @Override
    public Result createCourse(String token, Course course) {
        return null;
    }

    @Override
    public Result deleteCourse(int cid) {return null;}

    @Override
    public Result editCourse(Course course) {return null;}

    @Override
    public Result addCourse(String token, int cid) {
        return null;
    }

    @Override
    public Result removeCourse(String token, int cid) {
        return null;
    }

    @Override
    public Result getCourses(String token) {
        return null;
    }
}
