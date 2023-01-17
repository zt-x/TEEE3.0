package com.teee.controller.Account;

import com.teee.controller.ProjectCode;
import com.teee.vo.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @PostMapping("/register")
    public R register(@RequestParam String uid,@RequestParam String username){
        return new R(ProjectCode.CODE_EXCEPTION_BUSSINESS, null,"注册成功！");
    }
}
