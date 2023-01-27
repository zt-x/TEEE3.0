package com.teee.service;

import com.alibaba.fastjson.JSONObject;
import com.teee.domain.work.Work;
import com.teee.vo.Result;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Xu ZhengTao
 * @version 3.0
 */
public interface WorkService {


    /**
     * 获取作业内容
     * @authorization student
     * @return data:"questions:[{}{}{}{}]"
     */
    Result getWorkContent(int id);

    /**
     * 获取作业计时器
     * @authorization student
     * @param token: token
     * @param wid {"wid":xx}
     **/
    Result getWorkTimer(String token, int wid);

    /**
     * 提交作业
     * @authorization student
     * @param jo: {"token"(String):xx, "wid":xx, "ans"(String):"xx", "files"(String):"xxx"}
     * @return {code="", msg=""}
     * */
    Result submitWork(String token, JSONObject jo);

    /**
     * 布置作业
     * @authorization teacher
     * @param work: Work
     * */
    Result releaseWork(Work work);

    /**
     * 删除作业
     * @authorization teacher
     * @param jo:{"wid":xx}
     * */
    Result delWork(JSONObject jo);

    /**
     * 查看作业信息
     * @authorization teacher
     * @param jo:{"wid":xx}
     * */
    Result getWorkInfo(JSONObject jo);

    /**
     * 编辑作业
     * @authorization teacher
     * @param work:Work
     * */
    Result editWorkInfo(Work work);

    /**
     * 获取当前作业的所有提交物
     * @authorization teacher
     * @param jo:{"wid":xx}
     * @return
     * data<JSONArray>:[
     *      {submitId:, usernmae, uid:, finish_readover: , score: }
     *  ]
     * */
    Result getWorkSubmits(JSONObject jo);

    /**
     * 获取当前课程的所有作业的提交情况
     * @authorization teacher
     * @param jo:{"cid":xx}
     * */
    Result getCourseWorkFinishSituation(JSONObject jo);


    /**
     * 打包下载作业附件
     * @authorization teacher
     * @param wid
     * @return code
     * */
    Result downloadFiles(Integer wid, HttpServletResponse response);

    /**
     * 批改分数
     * @authorization teacher
     * @param jo: {int sid, String score}
     * @return code=1/-1, msg=""
     * */
    Result setSubmitScore(JSONObject jo);
    Result getWorkFinishStatus(String token, int cid);
}
