package com.teee.controller.Work;

import com.alibaba.fastjson.JSONObject;
import com.teee.project.Annoation.RoleCheck;
import com.teee.project.ProjectRole;
import com.teee.service.WorkService;
import com.teee.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/works/student")
@RoleCheck(role = ProjectRole.STUDENT)
public class StudentWorkController {

    @Autowired
    WorkService workService;

    @GetMapping("/status")
    Result getWorkFinishStatus(@RequestHeader("Authorization") String token, @RequestParam("cid") int cid){
        return workService.getWorkFinishStatus(token, cid);
    };

    @GetMapping("/content")
    Result getWorkContent(@RequestParam("wid") int wid){
        return workService.getWorkContent(wid);
    }

    @GetMapping("/qContent")
    Result getQueContent(@RequestParam("wid") int wid, @RequestParam("qid") int qid){
        return workService.getQueContent(wid, qid);
    }
    @GetMapping("/timer")
    Result getWorkTimer(@RequestHeader("Authorization") String token,@RequestParam("wid") int wid){
        return workService.getWorkTimer(token, wid);
    }

    /**
     * @params int wid, String ans, String files
     * */
    @PostMapping
    Result submitWork(@RequestHeader("Authorization") String token,@RequestBody JSONObject jo){
        return workService.submitWork(token, jo);
    }







}
