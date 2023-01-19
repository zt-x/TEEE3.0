package com.teee.controller.Course;

import com.alibaba.fastjson.JSONObject;
import com.teee.project.Annoation.RoleCheck;
import com.teee.project.ProjectRole;
import com.teee.service.CourseService;
import com.teee.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Xu ZhengTao
 *  student的课程controller
 *
 */
@RestController
@RequestMapping("/courses/student")
@RoleCheck(role = ProjectRole.STUDENT)
public class StudentCourseController {

    @Autowired
    CourseService courseService;

    @PostMapping
    public Result addCourse(@RequestHeader("Authorization") String token, @RequestBody JSONObject jo){
        return courseService.addCourse(token, jo);
    }
    @DeleteMapping
    public Result removeCourse(@RequestHeader("Authorization") String token, @RequestBody JSONObject jo){
        return courseService.removeCourse(token, jo);
    }
    @GetMapping
    public Result getMyCourses(@RequestHeader("Authorization") String token){
        return courseService.getCourses(token);
    }
}
