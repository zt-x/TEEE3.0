package com.teee.controller.Work;

import com.teee.project.Annoation.RoleCheck;
import com.teee.project.ProjectRole;
import com.teee.service.ExamService;
import com.teee.utils.MyAssert;
import com.teee.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exams")
public class ExamController {
    @Autowired
    ExamService examService;


    @RoleCheck(role = ProjectRole.STUDENT)
    @GetMapping("/validate/pre")
    Result getExamRulePre(@RequestParam("wid") int wid){
        MyAssert.notNull(wid,"这个作业发生了一些问题, 我们找不到该作业号 ...");
        return examService.getRuleForExam(wid);
    }
}
