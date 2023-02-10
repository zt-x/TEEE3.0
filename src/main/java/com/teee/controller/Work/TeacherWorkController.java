package com.teee.controller.Work;

import com.alibaba.fastjson.JSONObject;
import com.teee.domain.work.Work;
import com.teee.domain.work.WorkExamRule;
import com.teee.project.Annoation.RoleCheck;
import com.teee.project.ProjectRole;
import com.teee.service.ExamService;
import com.teee.service.SubmitService;
import com.teee.service.WorkService;
import com.teee.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/works/teacher")
@RoleCheck(role = ProjectRole.TEACHER)
public class TeacherWorkController {
    @Autowired
    WorkService workService;
    @Autowired
    ExamService examService;
    @Autowired
    SubmitService submitService;

    @GetMapping("/summary")
    public Result getAllWorkSummary(@RequestParam("cid") Integer cid){
        return workService.getAllWorkSummary(cid);
    }

    @PostMapping
    public Result release(@RequestBody Work work){
       return workService.releaseWork(work);
    }

    @PostMapping("/setRules")
    public Result setRules(@RequestBody WorkExamRule workExamRule){
        return examService.setRuleForExam(workExamRule);
    }

    @GetMapping("/rules")
    public Result getExamRulePre(@RequestParam("wid") Integer wid){
        return examService.getRuleForExam(wid);
    }

    @PostMapping("/downloadAll")
    public Result downloadFiles(@RequestParam("wid") Integer wid, HttpServletResponse response){
        return workService.downloadFiles(wid,response);
    }

    @DeleteMapping
    public Result delWork(@RequestBody JSONObject jo){
        return workService.delWork(jo);
    }
}
