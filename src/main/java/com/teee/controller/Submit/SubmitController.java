package com.teee.controller.Submit;

import com.alibaba.fastjson.JSONObject;
import com.teee.project.Annoation.RoleCheck;
import com.teee.project.ProjectRole;
import com.teee.service.SubmitService;
import com.teee.utils.MyAssert;
import com.teee.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submits")
@RoleCheck(role = ProjectRole.STUDENT)
public class SubmitController {
    @Autowired
    SubmitService submitService;

    /** 学生获取自己提交的作业记录*/
    @GetMapping("/byWid")
    public Result getSubmitByWorkId(@RequestHeader("Authorization") String token, @RequestParam("wid") int wid){
        return submitService.getSubmitByWorkId(token, wid);
    }

    @GetMapping("/bySid")
    public Result getSubmitBySubmitId(@RequestParam("sid") int sid){
        return submitService.getSubmitContentBySid(sid);
    }

    @GetMapping
    @RoleCheck(role = ProjectRole.TEACHER)
    public Result getAllSubmitByWorkId(@RequestParam("wid") int wid){
        return submitService.getAllSubmitByWorkId(wid);
    }

    @GetMapping("/summary")
    @RoleCheck(role = ProjectRole.TEACHER)
    public Result getSubmitSummary(@RequestParam("wid") int wid){
        return submitService.getSubmitSummary(wid);
    }

    @PutMapping("/teacher/score")
    @RoleCheck(role = ProjectRole.TEACHER)
    public Result setSubmitScore(@RequestBody JSONObject jo){
        return submitService.setSubmitScore(jo.getInteger("subid"), jo.getString("score"));
    }

    @PostMapping("/teacher/rejectSubmit")
    @RoleCheck(role = ProjectRole.TEACHER)
    public Result rejectSubmit(@RequestBody JSONObject jo) {
        Integer sid = jo.getInteger("sid");
        MyAssert.notNull(sid, "找不到这次提交记录, 可能已经被打回啦!");
        return submitService.rejectSubmit(sid);
    }

}
