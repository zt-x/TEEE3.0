package com.teee.domain.work;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("work_submit")
public class WorkSubmit {
    @TableId(type = IdType.AUTO)
    private  Integer sid;
    private Long uid;
    private Integer wid;

    private String uname;
    private Integer finishReadOver;
    private Float score;
    private Integer submitId;
    @TableLogic//逻辑删除
    private Integer deleted;
    @Version
    private Integer version;


    //public static int getNumOfQue(WorkSubmit sw){
    //    try{
    //        AWorkDao aWorkDao = SpringBeanUtil.getBean(AWorkDao.class);
    //        BankWorkDao bankWorkDao = SpringBeanUtil.getBean(BankWorkDao.class);
    //        Integer workTableId = sw.getWorkTableId();
    //        AWork aWork = aWorkDao.selectById(workTableId);
    //        BankWork bankWork = bankWorkDao.selectById(aWork.getWorkId());
    //        String questions = bankWork.getQuestions();
    //        JSONArray jsonArray = TypeChange.str2Jarr(questions);
    //        return jsonArray.size();
    //    }catch (Exception e){
    //        e.printStackTrace();
    //        return -1;
    //    }
    //}
    //public static JSONArray getWorkCotent(WorkSubmit sw){
    //    try{
    //        AWorkDao aWorkDao = SpringBeanUtil.getBean(AWorkDao.class);
    //        BankWorkDao bankWorkDao = SpringBeanUtil.getBean(BankWorkDao.class);
    //        Integer workTableId = sw.getWorkTableId();
    //        AWork aWork = aWorkDao.selectById(workTableId);
    //        BankWork bankWork = bankWorkDao.selectById(aWork.getWorkId());
    //        String questions = bankWork.getQuestions();
    //        JSONArray jsonArray = TypeChange.str2Jarr(questions);
    //        return jsonArray;
    //    }catch (Exception e){
    //        e.printStackTrace();
    //        return null;
    //    }
    //}
    //public static JSONArray getWorkCotentByWBID(Integer wbid){
    //    try{
    //        AWorkDao aWorkDao = SpringBeanUtil.getBean(AWorkDao.class);
    //        BankWorkDao bankWorkDao = SpringBeanUtil.getBean(BankWorkDao.class);
    //        BankWork bankWork = bankWorkDao.selectById(wbid);
    //        String questions = bankWork.getQuestions();
    //        JSONArray jsonArray = TypeChange.str2Jarr(questions);
    //        return jsonArray;
    //    }catch (Exception e){
    //        e.printStackTrace();
    //        return null;
    //    }
    //}

}
