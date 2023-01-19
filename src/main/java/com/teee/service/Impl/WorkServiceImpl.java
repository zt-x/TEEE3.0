package com.teee.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.teee.domain.work.Work;
import com.teee.domain.work.WorkExamRule;
import com.teee.service.WorkService;
import com.teee.vo.Result;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class WorkServiceImpl implements WorkService {

    // TODO

    @Override
    public Result getCourseWorks(JSONObject jo) {
        return null;
    }

    @Override
    public Result getWorkContent(JSONObject jo) {
        return null;
    }

    @Override
    public Result getWorkTimer(String token, JSONObject jo) {
        return null;
    }

    @Override
    public Result submitWork(JSONObject jo) {
        return null;
    }

    @Override
    public Result releaseWork(Work work) {
        return null;
    }

    @Override
    public Result delWork(JSONObject jo) {
        return null;
    }

    @Override
    public Result getWorkInfo(JSONObject jo) {
        return null;
    }

    @Override
    public Result editWorkInfo(Work work) {
        return null;
    }

    @Override
    public Result getWorkSubmits(JSONObject jo) {
        return null;
    }

    @Override
    public Result getCourseWorkFinishSituation(JSONObject jo) {
        return null;
    }

    @Override
    public Result setRules(WorkExamRule workExamRule) {
        return null;
    }

    @Override
    public Result getExamRulePre(Integer wid) {
        return null;
    }

    @Override
    public Result getExamRuleEnter(Integer wid) {
        return null;
    }

    @Override
    public Result downloadFiles(JSONObject jo, HttpServletResponse response) {
        return null;
    }

    @Override
    public Result setSubmitScore(JSONObject jo) {
        return null;
    }
}
