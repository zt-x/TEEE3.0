package com.teee.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.teee.dao.WorkDao;
import com.teee.dao.WorkSubmitContentDao;
import com.teee.dao.WorkSubmitDao;
import com.teee.domain.work.WorkSubmit;
import com.teee.domain.work.WorkSubmitContent;
import com.teee.project.ProjectCode;
import com.teee.utils.TypeChange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class AutoReadOver {
    @Autowired
    WorkSubmitContentDao workSubmitContentDao;
    @Autowired
    WorkDao workDao;
    @Autowired
    WorkSubmitDao workSubmitDao;


    // TODO 0 异步执行， 后续可能考虑使用RabbitMQ
    @Async
    public void autoReadOver(WorkSubmit workSubmit, boolean readChoice, boolean readFillIn) {
        // log.info("进入AutoReadOver :" + readChoice + "|" + readFillIn);
        WorkSubmit sw = workSubmit;
        Integer submitId = sw.getSid();
        WorkSubmitContent workSubmitContent = workSubmitContentDao.selectById(submitId);
        System.out.println(workSubmitContent);
        ArrayList<String> readOver;
        ArrayList<String> submitContent = TypeChange.str2arrl(workSubmitContent.getSubmitContent());
        String readover = workSubmitContent.getReadover();
        double factTotalScore = 0;
        if(readover.equals("")){
            int len = WorkSubmit.getNumOfQue(sw);
            readOver = new ArrayList<>(len);
            for(int i=0; i<len; i++){
                readOver.add(i,"");
            }
        }else {
            readOver = TypeChange.str2arrl(readover);
        }
        JSONArray workCotent = WorkSubmit.getWorkCotent(sw);
        JSONObject jo;
        if (workCotent != null) {
            for (int i=0;i<workCotent.size();i++) {
                jo = (JSONObject) workCotent.get(i);
                Float qscore = Float.valueOf(jo.get("qscore").toString());
                factTotalScore+=qscore;
                // 选择题
                if (jo.get("qtype").equals(ProjectCode.QueType_choice_question) && readChoice) {
                    Float score = -1f;
                    ArrayList<String> cans = TypeChange.str2arrl(jo.get("cans").toString(), ",");
                    ArrayList<String> ans = TypeChange.str2arrl(submitContent.get(i), ",");
                    //cans 是正确答案
                    //ans 是学生提交的答案
                    //ans中 出现 不属于cans 的 ，则0分，否则满分
                    boolean isErr = false;
                    for (String an : ans) {
                        if (!cans.contains(an)) {
                            score = 0f;
                            isErr = true;
                            break;
                        } else {
                            score = qscore;
                        }
                    }
                    if(ans.size() == 0){
                        isErr = true;
                        score = 0f;
                    }
                    if(!isErr){
                        if (ans.size() == cans.size()) {
                            score = qscore;
                        } else {
                            // 拿一半分
                            score = Float.valueOf(String.valueOf(qscore * 0.5));
                        }
                    }
                    readOver.set(i,String.format("%.2f", score));
                }
                // 填空题
                else if (jo.get("qtype").equals(ProjectCode.QueType_fillin_question) && readFillIn) {
                    try{
                        String ans = submitContent.get(i).replaceAll("&douhao;", ",");
                        String cans = jo.getString("cans");
                        if(cans.equals(ans)){
                            readOver.set(i, String.valueOf(qscore));
                        }else{
                            readOver.set(i, "0.0");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                //简答题
                else if (jo.get("qtype").equals(ProjectCode.QueType_text_question)) {
                    readOver.set(i, "-1");
                } else {
                    readOver.set(i, "-1");
                }
            }
            int finished = 1;
            Float finial_score = 0F;
            for (String s : readOver) {
                if(s.equals("-1")){
                    finished = 0;
                    break;
                }else{
                    finial_score += Float.parseFloat(s);
                }
            }

            // 计算分率 Rate = 总分/实际总分
            double rate = workDao.selectById(workSubmit.getWid()).getTotalScore() / factTotalScore;
            log.info("分率: " + rate);
            if(rate == 0){
                // ERR
                rate = 1;
            }
            workSubmitContent.setFinishReadOver(finished);
            sw.setScore((float) (finial_score*rate));
            sw.setFinishReadOver(finished);
            workSubmitContent.setReadover(TypeChange.arrL2str(readOver));
            try{
                workSubmitContentDao.updateById(workSubmitContent);
                workSubmitDao.updateById(sw);
                log.info("结束AutoReadOver");
                return;
            }catch (Exception e){
                log.info("结束AutoReadOver");
                return;
            }
        }else{
            log.info("结束AutoReadOver");
            log.info("WorkCointent 为 null");
        }
        log.info("结束AutoReadOver");
    }
}
