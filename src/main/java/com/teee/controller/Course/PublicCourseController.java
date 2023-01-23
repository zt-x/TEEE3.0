package com.teee.controller.Course;

import com.teee.project.Annoation.RoleCheck;
import com.teee.project.ProjectRole;
import com.teee.service.CourseService;
import com.teee.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
@RoleCheck(role = ProjectRole.STUDENT)
public class PublicCourseController {
    @Autowired
    CourseService courseService;

    @GetMapping
    public Result getMyCourses(@RequestHeader("Authorization") String token, @RequestParam("page") int page){
        return courseService.getCourses(token, page);
    }
    @GetMapping("/info")
    public Result getCourseInfo(@RequestParam("cid") int cid){
        return courseService.getCourseInfo(cid);
    }
    @GetMapping("/users")
    public Result getCourseUsers(@RequestParam("cid") int cid){
        return courseService.getUsers(cid);
    }
    @GetMapping("/works")
    public Result getWorks(@RequestParam("cid") int cid){
        return courseService.getWorks(cid);
    }

    @GetMapping("/announcements")
    public Result getAnnouncements(@RequestParam("cid") int cid){
        return courseService.getAnnouncements(cid);
    }
}
