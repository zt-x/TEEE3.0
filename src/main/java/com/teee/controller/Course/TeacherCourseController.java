package com.teee.controller.Course;

import com.alibaba.fastjson.JSONObject;
import com.teee.domain.course.Course;
import com.teee.project.Annoation.RoleCheck;
import com.teee.project.ProjectRole;
import com.teee.service.CourseService;
import com.teee.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

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


    @GetMapping("/LEStatistic")
    public Result createCourse(@RequestParam("cid") int cid){
        return courseService.getLastExamStatistics(cid);
    }
    @PostMapping
    public Result createCourse(@RequestHeader("Authorization") String token, @RequestBody Course course){
        return courseService.createCourse(token, course);
    }
    @PostMapping("/addUsers")
    public Result addUser(@RequestBody JSONObject jo){
        return courseService.addUsers(jo.getJSONArray("users"), jo.getInteger("cid"));
    }
    @DeleteMapping
    public Result delCourse(@RequestBody JSONObject jo){
        return courseService.delCourse((Integer)jo.get("cid"));
    }

    @DeleteMapping("/delUser")
    public Result delUserFromCourse(@RequestBody JSONObject jo){
        // TODO 0 这里写的不好看
        long uid = jo.getLong("uid");
        return courseService.removeUserFromCourse(uid, jo);
    }

    @PutMapping
    public Result editCourse( @RequestBody Course course){
        return courseService.editCourse(course);
    }

    @GetMapping("/downloadExportUser")
    public void downloadUserInfo(@RequestParam("cid") Integer cid, HttpServletResponse response) throws UnsupportedEncodingException {
        System.out.println("0");
        courseService.downloadUserInfo(cid, response);
        //return
    }
}
