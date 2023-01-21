package com.teee.service;


import com.alibaba.fastjson.JSONObject;
import com.teee.domain.course.Course;
import com.teee.vo.Result;

/**
 * @author Xu ZhengTao
 * @version 3.0
 */
public interface CourseService {
    // TODO
    Result getCourses(String token, int page);
    Result createCourse(String token, Course course);
    Result delCourse(int cid);
    Result editCourse(Course course);

    Result addCourse(String token, JSONObject jo);
    Result removeCourse(String token, JSONObject jo);
}
