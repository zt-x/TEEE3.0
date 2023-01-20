package com.teee.controller.Course;

import com.teee.project.Annoation.RoleCheck;
import com.teee.project.ProjectRole;
import com.teee.service.CourseService;
import com.teee.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
@RoleCheck(role = ProjectRole.STUDENT)
public class PublicCourseController {
    @Autowired
    CourseService courseService;

    @GetMapping
    public Result getMyCourses(@RequestHeader("Authorization") String token){
        return courseService.getCourses(token);
    }
}
