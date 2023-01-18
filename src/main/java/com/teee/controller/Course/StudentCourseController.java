package com.teee.controller.Course;

import com.teee.controller.Project.Annoation.RoleCheck;
import com.teee.controller.Project.ProjectRole;
import com.teee.vo.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xu ZhengTao
 *  student的课程controller
 *
 */
@RestController
@RequestMapping("/course/student")
@RoleCheck(role = ProjectRole.STUDENT)
public class StudentCourseController {

    public R addCourse(String token, int cid){

    }
    public R getMyCourses(String token){

    }
}
