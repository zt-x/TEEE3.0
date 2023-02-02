package com.teee.service;

import com.teee.domain.bank.BankWork;
import com.teee.vo.Result;

import java.util.ArrayList;

/**
 * @author Xu ZhengTao
 */
public interface WorkBankService{
    Result createWorkBank(BankWork bankWork, Long tid);
    Result getWorkBankContent(Long tid, Integer wbid);
    Result getWorkBankQuestions(int role, Integer wbid);
    Result deleteWorkBank(Integer work_id);
    Result editWorksBank(BankWork bankWork);
    BankWork getWorkBankById(Integer work_id);
    Result getWorkBankByOnwer(Long owner);
    Result addBankTags(Integer workId, ArrayList<String> tags);
    // 批改作业，返回得分
    //Float readOverWork(BankWork standardBankWork, WorkSubmit submitWork);
}