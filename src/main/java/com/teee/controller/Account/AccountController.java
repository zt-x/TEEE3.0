package com.teee.controller.Account;

import com.teee.controller.Project.Annoation.RoleCheck;
import com.teee.controller.Project.ProjectRole;
import com.teee.service.AccountService;
import com.teee.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;


    @PostMapping("/register")
    public R register(@RequestParam Long uid, @RequestParam String uname){
        return accountService.register(uid, uname);
    }

    @PostMapping("/login")
    public R login(@RequestParam Long uid, @RequestParam String pwd){
        return accountService.login(uid, pwd);
    }

    @GetMapping
    @RoleCheck(role = ProjectRole.STUDENT)
    public R getUserInfo(@RequestParam Long uid){
        return accountService.getUserInfo(uid);
    }

    @PutMapping
    @RoleCheck(role = ProjectRole.STUDENT)
    public R updateUserInfo(@RequestParam Long uid,@RequestParam String uname,@RequestParam String avatar){
        return accountService.updateUserInfo(uid, uname, avatar);
    }

    @DeleteMapping
    @RoleCheck(role = ProjectRole.STUDENT)
    public R delUser(@RequestParam Long uid){
        return accountService.delUser(uid);
    }

}
