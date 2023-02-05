package com.teee.domain.work;


import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.*;
import com.teee.dao.BankWorkDao;
import com.teee.dao.WorkDao;
import com.teee.domain.bank.BankWork;
import com.teee.utils.SpringBeanUtil;
import com.teee.utils.TypeChange;
import lombok.Data;

@Data
@TableName("work_submit")
public class WorkSubmit {
    @TableId
    private  Integer sid;
    private Long uid;
    private Integer wid;

    private String uname;
    private Integer finishReadOver;
    private Float score;
    @TableLogic//逻辑删除
    private Integer deleted;
    @Version
    private Integer version;



    public static int getNumOfQue(WorkSubmit sw){
        try{
            WorkDao workDao = SpringBeanUtil.getBean(WorkDao.class);
            BankWorkDao bankWorkDao = SpringBeanUtil.getBean(BankWorkDao.class);
            Integer wid = sw.getWid();
            Work work = workDao.selectById(wid);
            BankWork bankWork = bankWorkDao.selectById(work.getBwid());
            String questions = bankWork.getQuestions();
            JSONArray jsonArray = TypeChange.str2Jarr(questions);
            return jsonArray.size();
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    public static JSONArray getWorkCotent(WorkSubmit sw){
        try{
            WorkDao workDao = SpringBeanUtil.getBean(WorkDao.class);
            BankWorkDao bankWorkDao = SpringBeanUtil.getBean(BankWorkDao.class);
            Integer wid = sw.getWid();
            Work work = workDao.selectById(wid);
            BankWork bankWork = bankWorkDao.selectById(work.getBwid());
            String questions = bankWork.getQuestions();
            JSONArray jsonArray = TypeChange.str2Jarr(questions);
            return jsonArray;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static JSONArray getWorkCotentByWBID(Integer wbid){
        try{
            WorkDao workDao = SpringBeanUtil.getBean(WorkDao.class);
            BankWorkDao bankWorkDao = SpringBeanUtil.getBean(BankWorkDao.class);
            BankWork bankWork = bankWorkDao.selectById(wbid);
            String questions = bankWork.getQuestions();
            JSONArray jsonArray = TypeChange.str2Jarr(questions);
            return jsonArray;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
