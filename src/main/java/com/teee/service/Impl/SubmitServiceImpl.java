package com.teee.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.teee.dao.WorkSubmitDao;
import com.teee.domain.work.WorkSubmit;
import com.teee.project.ProjectCode;
import com.teee.service.SubmitService;
import com.teee.utils.JWT;
import com.teee.vo.Result;
import com.teee.vo.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubmitServiceImpl implements SubmitService {

    @Autowired
    WorkSubmitDao workSubmitDao;


    @Override
    public Result SubmitWork(String token, int wid, String ans, String files) {
        return null;
    }

    @Override
    public Result getAllSubmitByWorkId(int wid) {
        return null;
    }

    @Override
    public Result setSubmitScore(int subid, String score) {
        return null;
    }

    @Override
    public Result getSubmitSummary(int subid) {
        return null;
    }

    @Override
    public Result getSubmitContentBySid(int sid) {
        return null;
    }

    @Override
    public Result getSubmitByWorkId(String token, int wid) {
        try{
            Long uid = JWT.getUid(token);
            WorkSubmit submitWork = workSubmitDao.selectOne(new LambdaQueryWrapper<WorkSubmit>().eq(WorkSubmit::getUid, uid).eq(WorkSubmit::getWid, wid));
            return new Result(ProjectCode.CODE_SUCCESS, JSONObject.toJSONString(submitWork), "获取成功");
        }catch (Exception e){
            throw new BusinessException(ProjectCode.CODE_EXCEPTION, "获取作业的提交记录时发生错误", e);
        }
    }
}
