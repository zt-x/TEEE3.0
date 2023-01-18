package com.teee.controller.Work;

import com.alibaba.fastjson2.JSONObject;
import com.teee.project.Annoation.RoleCheck;
import com.teee.project.ProjectRole;
import com.teee.service.WorkService;
import com.teee.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/works/student")
@RoleCheck(role = ProjectRole.STUDENT)
public class StudentWorkController {

    @Autowired
    WorkService workService;

    Result getCourseWorks(@RequestBody JSONObject jo){
        return workService.getCourseWorks(jo);
    }

    Result getWorkContent(@RequestBody JSONObject jo){
        return workService.getWorkContent(jo);
    }





}
