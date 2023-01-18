package com.teee.controller.Account;

import com.teee.project.Annoation.RoleCheck;
import com.teee.project.ProjectRole;
import com.teee.service.AccountService;
import com.teee.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;


    @PostMapping("/register")
    public Result register(@RequestParam Long uid, @RequestParam String uname){
        return accountService.register(uid, uname);
    }

    @PostMapping("/login")
    public Result login(@RequestParam Long uid, @RequestParam String pwd){
        return accountService.login(uid, pwd);
    }

    @GetMapping
    @RoleCheck(role = ProjectRole.STUDENT)
    public Result getUserInfo(@RequestParam Long uid){
        return accountService.getUserInfo(uid);
    }

    @PutMapping
    @RoleCheck(role = ProjectRole.STUDENT)
    public Result updateUserInfo(@RequestParam Long uid, @RequestParam String uname, @RequestParam String avatar){
        return accountService.updateUserInfo(uid, uname, avatar);
    }

    @DeleteMapping
    @RoleCheck(role = ProjectRole.STUDENT)
    public Result delUser(@RequestParam Long uid){
        return accountService.delUser(uid);
    }

}
