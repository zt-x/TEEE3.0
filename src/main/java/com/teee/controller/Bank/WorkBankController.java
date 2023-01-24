package com.teee.controller.Bank;

import com.teee.domain.bank.BankWork;
import com.teee.service.WorkBankService;
import com.teee.utils.JWT;
import com.teee.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/banks/work")
public class WorkBankController {

    @Autowired
    WorkBankService workBankService;

    @PostMapping
    public Result addWorkBank(@RequestHeader("Authorization") String token, @RequestBody BankWork bankWork){
        Long tid = JWT.getUid(token);
        return workBankService.createWorkBank(bankWork, tid);
    }

    @GetMapping()
    public Result getWorkBankByTid(String token){
        return workBankService.getWorkBankByOnwer(JWT.getUid(token));
    }
    @GetMapping("/content")
    public Result getWorkBankContentById(String token, Integer wbid){
        return workBankService.getWorkBankContent(JWT.getUid(token),wbid);
    }
    @PutMapping
    public Result editWorkBank(BankWork bankWork){
        return workBankService.editWorksBank(bankWork);
    }
    @DeleteMapping
    public Result deleteWorkBank(Integer wbid){
        return workBankService.deleteWorkBank(wbid);
    }

}
