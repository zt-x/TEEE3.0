package com.teee.service;


import com.teee.domain.course.Course;
import com.teee.vo.Result;

/**
 * @author Xu ZhengTao
 * @version 3.0
 */
public interface CourseService {
    // TODO
    Result getCourses(String token);

    //Teacher

    Result createCourse(String token, Course course);

    Result deleteCourse(int cid);

    Result editCourse(Course course);
    //Student

    Result addCourse(String token, int cid);

    Result removeCourse(String token, int cid);


}
