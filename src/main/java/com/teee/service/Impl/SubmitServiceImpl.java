package com.teee.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.teee.dao.*;
import com.teee.domain.work.Work;
import com.teee.domain.work.WorkSubmit;
import com.teee.domain.work.WorkSubmitContent;
import com.teee.project.ProjectCode;
import com.teee.service.SubmitService;
import com.teee.utils.JWT;
import com.teee.utils.MyAssert;
import com.teee.utils.TypeChange;
import com.teee.vo.Result;
import com.teee.vo.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SubmitServiceImpl implements SubmitService {

    @Autowired
    WorkSubmitDao workSubmitDao;
    @Autowired
    WorkSubmitContentDao workSubmitContentDao;
    @Autowired
    UserInfoDao userInfoDao;
    @Autowired
    CourseUserDao courseUserDao;
    @Autowired
    WorkDao workDao;

    @Override
    public Result getAllSubmitByWorkId(int wid) {
        LambdaQueryWrapper<WorkSubmit> lqw = new LambdaQueryWrapper();
        lqw.eq(WorkSubmit::getWid, wid);
        List<WorkSubmit> workSubmits = workSubmitDao.selectList(lqw);
        ArrayList<JSONObject> jarr = new ArrayList<>();
        JSONObject jb;
        for (WorkSubmit workSubmit : workSubmits) {
            jb = (JSONObject) JSONObject.toJSON(workSubmit);
            jb.put("avatar", userInfoDao.selectById(workSubmit.getUid()).getAvatar());
            jarr.add(jb);
        }
        return new Result(ProjectCode.CODE_SUCCESS, jarr, "获取成功");
    }

    @Override
    public Result setSubmitScore(int subid, String score) {
        try{
            // 重置Submit_work_content表的readover
            WorkSubmitContent workSubmitContent = workSubmitContentDao.selectById(subid);
            WorkSubmit workSubmit = workSubmitDao.selectOne(new LambdaQueryWrapper<WorkSubmit>().eq(WorkSubmit::getSid, subid));
            // 重新计算Submit_work表的Score
            int finish_readover = 1;
            float total_score = 0f;
            double factTotalScore = 0;

            //ArrayList<String> arrayList = TypeChange.str2arrl("["+score+"]", ",");

            ArrayList<String> arrayList = TypeChange.str2arrl(score);
            for (int i=0;i<arrayList.size();i++) {
                if(Float.parseFloat(arrayList.get(i)) == -1){
                    finish_readover = 0;
                }
                total_score += Float.parseFloat(arrayList.get(i));
                arrayList.set(i, String.format("%.2f", (Float.parseFloat(arrayList.get(i)))));
            }
            JSONArray workCotent = WorkSubmit.getWorkCotent(workSubmit);
            MyAssert.notNull(workCotent, "作业内容不存在 ...");
            JSONObject jo;
            assert workCotent != null;
            for (Object o : workCotent) {
                jo = (JSONObject) o;
                float qscore = Float.parseFloat(jo.get("qscore").toString());
                factTotalScore += qscore;
            }

            workSubmitContent.setReadover(TypeChange.arrL2str(arrayList));
            double rate = workDao.selectById(workSubmit.getWid()).getTotalScore() / factTotalScore;
            workSubmit.setScore((float) (total_score*rate));
            workSubmit.setFinishReadOver(finish_readover);
            workSubmitContent.setFinishReadOver(finish_readover);
            workSubmitContentDao.updateById(workSubmitContent);

            workSubmitDao.updateById(workSubmit);
            log.info("subworkID=" + workSubmit.getSid() + "被改变了");
            return new Result(ProjectCode.CODE_SUCCESS, null, "批改成功!");
        }catch (Exception e){
            throw new BusinessException("批改作业时出了一点问题 ...",e);
        }
    }

    @Override
    public Result getSubmitSummary(int wid) {
        float total_score;
        String workname;
        int submit_submitedNum;
        int submit_totalNum;
        int readOver_done;
        int readOver_total;
        // NUM of people;
        int NOP_excellent;
        int NOP_good;
        int NOP_NTB; // not too bad
        int NOP_fail;
        try{
            Work work = workDao.selectById(wid);
            total_score = work.getTotalScore();
            workname = work.getWname();
            Integer cid = work.getCid();
            String uids = courseUserDao.selectById(cid).getUid();

            submit_totalNum = uids.length() - uids.replaceAll(",", "").length() + 1;
            submit_submitedNum = workSubmitDao.selectCount(new LambdaQueryWrapper<WorkSubmit>().eq(WorkSubmit::getWid, wid));

            readOver_done = workSubmitDao.selectCount(new LambdaQueryWrapper<WorkSubmit>().eq(WorkSubmit::getFinishReadOver, 1));
            readOver_total = submit_submitedNum;

            NOP_excellent = workSubmitDao.selectCount(new LambdaQueryWrapper<WorkSubmit>().eq(WorkSubmit::getWid, wid).between(WorkSubmit::getScore, total_score*0.9, total_score));
            NOP_good = workSubmitDao.selectCount(new LambdaQueryWrapper<WorkSubmit>().eq(WorkSubmit::getWid, wid).between(WorkSubmit::getScore, total_score*0.75, total_score*0.9-0.0001));
            NOP_NTB = workSubmitDao.selectCount(new LambdaQueryWrapper<WorkSubmit>().eq(WorkSubmit::getWid, wid).between(WorkSubmit::getScore, total_score*0.6, total_score*0.75-0.0001));
            NOP_fail = workSubmitDao.selectCount(new LambdaQueryWrapper<WorkSubmit>().eq(WorkSubmit::getWid, wid).between(WorkSubmit::getScore, total_score*0, total_score*0.6-0.0001));

            //包装 返回
            String ret = "{\"total_score\": \"" + total_score + "\", \"workname\": \"" + workname + "\", \"submit_submitedNum\": \"" + submit_submitedNum
                    + "\", \"submit_totalNum\": \"" + submit_totalNum + "\", \"readOver_done\": \"" + readOver_done + "\", \"readOver_total\": \"" + readOver_total
                    + "\", \"NOP_excellent\": \"" + NOP_excellent+ "\", \"NOP_good\": \"" + NOP_good+ "\", \"NOP_NTB\": \"" + NOP_NTB+ "\", \"NOP_fail\": \"" + NOP_fail+"\"}";

            return new Result(ProjectCode.CODE_SUCCESS, ret, "获取提交作业信息成功捏");
        }catch(Exception e){
           throw new BusinessException("获取提交作业信息时出了点问题 ...");
        }
    }

    @Override
    public Result getSubmitContentBySid(int sid) {
        try{

            WorkSubmitContent workSubmitContent = workSubmitContentDao.selectById(sid);

            // 给Content的每一项加上引号
            ArrayList<String> arrayList = TypeChange.str2arrl(workSubmitContent.getSubmitContent(), ", ");
            for (int i = 0; i < arrayList.size(); i++) {
                arrayList.set(i, "\"" + arrayList.get(i) + "\"");
            }
            workSubmitContent.setSubmitContent(arrayList.toString());

            // 给Files的每一项加上引号
            ArrayList<String> arrayList2 = TypeChange.str2arrl(workSubmitContent.getFiles(), ", ");
            for (int i = 0; i < arrayList2.size(); i++) {
                arrayList2.set(i, "\"" + arrayList2.get(i) + "\"");
            }
            workSubmitContent.setFiles(arrayList2.toString());

            return new Result(ProjectCode.CODE_SUCCESS, JSONObject.toJSONString(workSubmitContent), "获取sid=" + sid + "的数据成功");
        }catch (Exception e){
            throw new BusinessException("获取提交内容时出错 ... ",e);
        }
    }

    @Override
    public Result getSubmitByWorkId(String token, int wid) {
        try{
            Long uid = JWT.getUid(token);
            WorkSubmit workSubmit = workSubmitDao.selectOne(new LambdaQueryWrapper<WorkSubmit>().eq(WorkSubmit::getUid, uid).eq(WorkSubmit::getWid, wid));
            return new Result(ProjectCode.CODE_SUCCESS, JSONObject.toJSONString(workSubmit), "获取成功");
        }catch (Exception e){
            throw new BusinessException(ProjectCode.CODE_EXCEPTION, "获取作业的提交记录时发生错误", e);
        }
    }

    @Override
    public Result rejectSubmit(Integer sid) {
        int i = workSubmitDao.deleteById(sid);
        return new Result("操作成功!");
    }
}
