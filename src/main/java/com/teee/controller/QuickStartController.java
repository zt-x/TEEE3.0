package com.teee.controller;

import com.teee.project.ProjectCode;
import com.teee.service.CourseService;
import com.teee.service.WorkBankService;
import com.teee.utils.JWT;
import com.teee.vo.Result;
import com.teee.vo.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qstarts")
public class QuickStartController {
    @Autowired
    CourseService courseService;
    @Autowired
    WorkBankService workBankService;

    @GetMapping("/todo")
    Result getTodoList(@RequestHeader("Authorization") String token){
        if(JWT.isStudent(token)){
            //TODO student的TODO
            return courseService.getCoursesTodo(token);

        }else if (JWT.isTeacher(token)){
            //TODO teacher的TODO
            return courseService.getCoursesTodo(token);
        }else if(JWT.isAdmin(token)){
            //TODO Admin的TODO
        }else{
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_TOKENILLEGAL);
        }
        return null;
    }
    @GetMapping("/bank")
    Result getBankSummary(@RequestHeader("Authorization") String token){
        if (JWT.isTeacher(token)) {
            return workBankService.getMyBankSummary(JWT.getUid(token));
        }
        return null;
    }
}
