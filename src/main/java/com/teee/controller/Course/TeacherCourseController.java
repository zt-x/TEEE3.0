package com.teee.controller.Course;

import com.alibaba.fastjson2.JSONObject;
import com.teee.domain.course.Course;
import com.teee.project.Annoation.RoleCheck;
import com.teee.project.ProjectRole;
import com.teee.service.CourseService;
import com.teee.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Xu ZhengTao
 * @version 3.0
 */
@RestController
@RequestMapping("/courses/teacher")
@RoleCheck(role = ProjectRole.TEACHER)
public class TeacherCourseController {

    @Autowired
    CourseService courseService;

    @PostMapping
    public Result createCourse(@RequestHeader("Authorization") String token, @RequestBody Course course){
        return courseService.createCourse(token, course);
    }

    @DeleteMapping
    public Result deleteCourse(@RequestBody JSONObject jo){
        return courseService.deleteCourse((Integer)jo.get("cid"));
    }

    @PutMapping
    public Result editCourse( @RequestBody Course course){
        return courseService.editCourse(course);
    }

}
