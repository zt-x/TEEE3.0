package com.teee.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.teee.api.in.Tencent;
import com.teee.dao.UserFaceDao;
import com.teee.dao.WorkExamRuleDao;
import com.teee.domain.user.UserFace;
import com.teee.domain.work.WorkExamRule;
import com.teee.project.ProjectCode;
import com.teee.service.ExamService;
import com.teee.utils.MyAssert;
import com.teee.utils.TypeChange;
import com.teee.vo.Result;
import com.teee.vo.exception.BusinessException;
import com.teee.vo.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author Xu ZhengTao
 * @version 3.0
 */
@Service
@Slf4j
public class ExamServiceImpl implements ExamService {

    @Autowired
    UserFaceDao userFaceDao;

    @Autowired
    Tencent tencent;

    @Autowired
    WorkExamRuleDao workExamRuleDao;


    @Override
    public Result setRuleForExam(WorkExamRule rule) {
        try {
            if (workExamRuleDao.selectCount(new LambdaQueryWrapper<WorkExamRule>().eq(WorkExamRule::getWid, rule.getWid())) > 0) {
                workExamRuleDao.updateById(rule);
            } else {
                workExamRuleDao.insert(rule);
            }
            return new Result(ProjectCode.CODE_SUCCESS, "设置成功");
        } catch (Exception e) {
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "在设置考试规则时出错", e);
        }

    }

    @Override
    public Result getRuleForExam(int wid) {
        try {
            WorkExamRule workExamRule = workExamRuleDao.selectById(wid);
            if (workExamRule != null) {
                return new Result(ProjectCode.CODE_SUCCESS, workExamRule, "获取成功");
            } else {
                return new Result(ProjectCode.CODE_SUCCESS, new WorkExamRule(wid, "", "", "无规则"), "获取成功");
            }
        } catch (Exception e) {
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "在获取考试规则时出错", e);
        }
    }

    @Override
    public Result faceCheck(Long uid, String imgUrl) {
        // 本地拉取用户Face
        UserFace uFace = userFaceDao.selectById(uid);
        if (uFace == null || uFace.getFaceSrc() == null) {
            return new Result(ProjectCode.CODE_SUCCESS, "您还没有登记人脸信息~请先 “头像->个人信息->添加人脸验证” 喵");
        }
        log.info("正在获取本地FACE");
        String faceSrc = uFace.getFaceSrc();
        // 获取当前照片
        log.info("正在获取当前上传的FACE");
        if (faceSrc == null || imgUrl == null) {
            throw new BusinessException("两张照片中有一张的数据无法读取 ...");
        }
        Result faceCheckRes = tencent.faceCheck(faceSrc, imgUrl);
        if (faceCheckRes.getCode() > 0) {
            JSONObject data = (JSONObject) faceCheckRes.getData();
            BigDecimal score = TypeChange.Obj2BigDec(data.get("Score"));
            log.info("SCORE = " + score);
            if (score.compareTo(new BigDecimal(80)) >= 0) {
                return new Result("验证通过");
            } else {
                return new Result(ProjectCode.CODE_EXCEPTION, "验证不通过");
            }
        } else {
            return new Result(ProjectCode.CODE_EXCEPTION, faceCheckRes.getMsg());
        }
    }

    /**
     * submit:{
     * uid:
     * wid:
     * subFace:
     * }
     **/

    @Override
    public Result checkRule(JSONObject submit, ArrayList<String> rules) {
        boolean pass = true;
        for (String rule : rules) {
            log.info("=== 循环");
            if ("FACKCHECK".equals(rule)) {
                // 本地拉取用户Face
                UserFace uFace = userFaceDao.selectById(submit.getString("uid"));
                MyAssert.notNull(uFace, "您还没有登记人脸信息捏~");
                log.info("正在获取本地FACE");
                String faceSrc;
                try {
                    faceSrc = TypeChange.getImageBaseURL(uFace.getFaceSrc());
                } catch (Exception e) {
                    throw new SystemException("转换本地face数据为base64时出错", e);
                }
                // 获取当前照片
                log.info("正在获取当前上传的FACE");
                // TODO 测试
                String submitFace = TypeChange.getImgBaseFile(submit.getString("subFace"));
                MyAssert.notNull(faceSrc,"存在空照片");
                Result faceCheckRes = tencent.faceCheck(faceSrc, submitFace);
                if (faceCheckRes.getCode() > 0) {
                    JSONObject data = (JSONObject) faceCheckRes.getData();
                    BigDecimal score = TypeChange.Obj2BigDec(data.get("Score"));
                    log.info("SCORE = " + score);
                    if (score.compareTo(new BigDecimal(80)) >= 0) {
                        return new Result("验证通过");
                    } else {
                        return new Result(ProjectCode.CODE_EXCEPTION, "相似度较低");
                    }
                } else {
                    return new Result(ProjectCode.CODE_EXCEPTION, faceCheckRes.getMsg());
                }
            }
        }
        return pass ?new Result("通过所有验证"):new Result(ProjectCode.CODE_EXCEPTION,"存在未通过的测试");
    }
}