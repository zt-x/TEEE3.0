package com.teee.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.teee.dao.*;
import com.teee.domain.bank.BankWork;
import com.teee.domain.user.UserInfo;
import com.teee.domain.work.*;
import com.teee.vo.exception.BusinessException;
import com.teee.project.ProjectCode;
import com.teee.service.CourseService;
import com.teee.service.WorkService;
import com.teee.util.JWT;
import com.teee.util.MyAssert;
import com.teee.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@Service
public class WorkServiceImpl implements WorkService {

    // TODO
    @Autowired
    CourseService courseService;
    @Autowired
    WorkSubmitDao workSubmitDao;
    @Autowired
    WorkDao workDao;
    @Autowired
    BankWorkDao bankWorkDao;
    @Autowired
    WorkTimerDao workTimerDao;
    @Autowired
    WorkSubmitContentDao workSubmitContentDao;
    @Autowired
    UserInfoDao userInfoDao;

    AutoReadOver autoReadOver;



    @Override
    public Result getWorkContent(int id) {

        Work aWork = workDao.selectById(id);
        MyAssert.notNull(aWork,"作业不存在😮");
        try{
            BankWork bankWork = bankWorkDao.selectById(aWork.getBwid());
            MyAssert.notNull(bankWork, "作业内容不存在😮");
            String bakQue = bankWork.getQuestions().replaceAll(",\\\\\\\"cans\\\\\\\":\\\\\\\".+\\\\\"", "");
            return new Result(ProjectCode.CODE_SUCCESS,bakQue,"获取成功");
        }catch (Exception e){
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "获取作业内容时异常", e);
        }
    }
    @Override
    public Result getWorkTimer(String token, int wid) {
        try{
            // 获取WorkTimer
            Long uid = JWT.getUid(token);
            WorkTimer workTimer = workTimerDao.selectOne(new LambdaQueryWrapper<WorkTimer>().eq(WorkTimer::getUid, uid).eq(WorkTimer::getWid, wid));
            if(workTimer == null){
                /************
                 第一次进入
                 ************/
                workTimer = new WorkTimer();
                workTimer.setUid(uid);
                workTimer.setWid(wid);
                Work aWork = workDao.selectOne(new LambdaQueryWrapper<Work>().eq(Work::getId, wid));
                MyAssert.notNull(aWork, "创建Timer时错误：无法找到作业");
                try{
                    Float timeLimit = aWork.getTimeLimit();
                    workTimer.setRestTime(String.valueOf(timeLimit*60.0));
                }catch (NullPointerException npe){
                    workTimer.setRestTime("无限制");
                }
                workTimerDao.insert(workTimer);
            }
            return new Result(ProjectCode.CODE_SUCCESS, workTimer.getRestTime(), "获取Timer成功");
        }catch (Exception e){
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "创建Timer时出错", e);
        }
    }

    @Override
    public Result submitWork(String token, JSONObject jo) {
        int wid = jo.getInteger("wid");
        String ans = jo.getString("ans");
        String files= jo.getString("files");
        WorkSubmitContent submitWorkContent = new WorkSubmitContent();
        submitWorkContent.setSubmitContent(ans);
        submitWorkContent.setReadover("");
        submitWorkContent.setFinishReadOver(0);
        //files: [["", "", ""],[]]
        submitWorkContent.setFiles("".equals(files)?"[]":files);
        workSubmitContentDao.insert(submitWorkContent);
        Integer submitId = submitWorkContent.getSid();
        WorkSubmit submitWork = new WorkSubmit();
        Long uid = JWT.getUid(token);
        submitWork.setUid(uid);
        submitWork.setWid(wid);
        submitWork.setScore(0F);
        submitWork.setSid(submitId);
        try {
            UserInfo userInfo = userInfoDao.selectById(uid);
            submitWork.setUname(userInfo.getUname());
            workSubmitDao.delete(new LambdaQueryWrapper<WorkSubmit>().eq(WorkSubmit::getUid, submitWork.getUid()).eq(WorkSubmit::getWid, submitWork.getWid()));
            workSubmitDao.insert(submitWork);
            boolean readChoice = (workDao.selectById(submitWork.getWid()).getAutoReadoverChoice() == 1);
            boolean readFillIn = (workDao.selectById(submitWork.getWid()).getAutoReadoverFillIn() == 1);
            try{
                autoReadOver.autoReadOver(submitWork, readChoice, readFillIn);
            }catch(Exception e){
                throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "作业提交成功, 但在自动批改阶段系统出现了一些问题, 具体情况请查看答题卡", e);
            }
            return new Result(ProjectCode.CODE_SUCCESS, null, "提交成功");
        }catch (Exception e){
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "😫系统在提交过程出了些问题", e);

        }
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

    @Override
    public Result getWorkFinishStatus(String token, int cid) {
        Long uid = JWT.getUid(token);
        JSONArray jarr = (JSONArray) courseService.getWorks(cid).getData();
        JSONArray jarr2 = new JSONArray();

        // [{wid:, status: ,score:}]
        // -1 未提交
        // 0 批改中
        // 1 已完成批改
        for (Object o : jarr) {
            JSONObject jo1 =  (JSONObject)o;
            JSONObject jo2 = new JSONObject();
            Integer id = (Integer) jo1.get("id");
            jo2.put("wid", id);
            WorkSubmit submitWork = workSubmitDao.selectOne(new LambdaQueryWrapper<WorkSubmit>().eq(WorkSubmit::getSid, id).eq(WorkSubmit::getUid, uid));
            if(submitWork == null){
                // 未提交
                jo2.put("status", -1);
                jo2.put("score", 0);
            }else{
                if(submitWork.getFinishReadOver() == 0){
                    jo2.put("status", 0);
                    jo2.put("score", submitWork.getScore());
                }else{
                    jo2.put("status", 1);
                    jo2.put("score", submitWork.getScore());
                }
            }
            jarr2.add(jo2);
        }
        return new Result(ProjectCode.CODE_SUCCESS, new ArrayList<String>(jarr2).toString(), "获取作业完成状态成功!");
    }
}
