package com.teee.controller.Course;

import com.alibaba.fastjson.JSONObject;
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
    public Result delCourse(@RequestBody JSONObject jo){
        return courseService.delCourse((Integer)jo.get("cid"));
    }

    @DeleteMapping("/delUser")
    public Result delUserFromCourse(@RequestBody JSONObject jo){
        // TODO 这里写的不好看
        return courseService.removeUserFromCourse(jo.getLong("uid"), jo);
    }

    @PutMapping
    public Result editCourse( @RequestBody Course course){
        return courseService.editCourse(course);
    }

}
