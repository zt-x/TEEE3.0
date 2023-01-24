package com.teee.service;

import com.alibaba.fastjson.JSONObject;
import com.teee.domain.work.WorkExamRule;
import com.teee.vo.Result;

import java.util.ArrayList;



public interface ExamService {

    Result setRuleForExam(WorkExamRule rule);
    Result getRuleForExam(int wid);
    Result checkRule(JSONObject submit, ArrayList<String> rules);
    Result faceCheck(Long uid, String imgUrl);

}
