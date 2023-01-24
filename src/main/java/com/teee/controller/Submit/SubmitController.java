package com.teee.controller.Submit;

import com.teee.project.Annoation.RoleCheck;
import com.teee.project.ProjectRole;
import com.teee.service.SubmitService;
import com.teee.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submits")
@RoleCheck(role = ProjectRole.STUDENT)
public class SubmitController {
    @Autowired
    SubmitService submitService;

    @GetMapping("/byWid")
    public Result getSubmitByWorkId(@RequestHeader("Authorization") String token, @RequestParam("wid") int wid){
        return submitService.getSubmitByWorkId(token, wid);
    }



}
