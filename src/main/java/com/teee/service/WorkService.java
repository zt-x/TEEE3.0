package com.teee.service;

import com.alibaba.fastjson2.JSONObject;
import com.teee.domain.work.Work;
import com.teee.vo.Result;
import org.springframework.web.bind.annotation.RequestBody;

public interface WorkService {

    /**
     * 获取当前课程下的所有作业
     * @authorization student/teacher
     * @param jo: {"cid":xx}
     * @return data:[Work1{}, Work2{}....]
     * */
    Result getCourseWorks(JSONObject jo);

    /**
     * 获取作业内容
     * @authorization student
     * @param jo: {"wid":xx}
     * @return data:"questions:[{}{}{}{}]"
     * */
    Result getWorkContent(JSONObject jo);

    /**
     * 提交作业
     * @authorization student
     * @param jo: {"token"(String):xx, "wid":xx, "ans"(String):"xx", "files"(String):"xxx"}
     * @return {code="", msg=""}
     * */
    Result submitWork(JSONObject jo);

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

}
