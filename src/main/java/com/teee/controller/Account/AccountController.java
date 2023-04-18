package com.teee.controller.Account;

import com.alibaba.fastjson.JSONObject;
import com.teee.project.Annoation.RoleCheck;
import com.teee.project.ProjectRole;
import com.teee.service.AccountService;
import com.teee.utils.JWT;
import com.teee.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;


    @PostMapping("/register")
    @RoleCheck(role = ProjectRole.TEACHER)
    public Result register(@RequestBody JSONObject jo){
        return accountService.register(jo);
    }
    @PostMapping("/resetPassword")
    @RoleCheck(role = ProjectRole.STUDENT)
    public Result resetPassword(@RequestHeader("Authorization") String token, @RequestBody JSONObject jo){
        return accountService.resetPassword(JWT.getUid(token), jo);
    }
    @PostMapping("/login")
    public Result login(@RequestBody JSONObject jo){
        return accountService.login(jo);
    }

    @GetMapping
    @RoleCheck(role = ProjectRole.STUDENT)
    public Result getUserInfo(@RequestHeader("Authorization") String token,@RequestParam(required = false, value = "uid") Long uid){
        if(uid == null || uid==-1){
            uid = JWT.getUid(token);
        }
        return accountService.getUserInfo(uid);
    }

    @GetMapping("/routes")
    @RoleCheck(role = ProjectRole.STUDENT)
    public Result getRoutes(@RequestHeader("Authorization") String token){
        return accountService.getBaseUserInfo(token);
    }

    @PutMapping
    @RoleCheck(role = ProjectRole.STUDENT)
    /** jo:{uid, avatar} */
    public Result updateUserInfo(@RequestBody JSONObject jo){
        return accountService.updateUserInfo(jo);
    }

    @DeleteMapping
    @RoleCheck(role = ProjectRole.TEACHER)
    /** jo:{uid: uid} */
    public Result delUser(@RequestBody JSONObject jo){
        return accountService.delUser(jo);
    }

}
