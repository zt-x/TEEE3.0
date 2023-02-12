package com.teee.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.teee.dao.BankOwnerDao;
import com.teee.dao.BankWorkDao;
import com.teee.dao.UserInfoDao;
import com.teee.dao.WorkDao;
import com.teee.domain.bank.BankOwner;
import com.teee.domain.bank.BankWork;
import com.teee.domain.user.UserInfo;
import com.teee.domain.work.Work;
import com.teee.domain.work.WorkSubmit;
import com.teee.project.Annoation.RoleCheck;
import com.teee.project.ProjectCode;
import com.teee.project.ProjectRole;
import com.teee.service.WorkBankService;
import com.teee.utils.MyAssert;
import com.teee.utils.TypeChange;
import com.teee.vo.Result;
import com.teee.vo.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class WorkBankServiceImpl implements WorkBankService {

    @Autowired
    BankWorkDao bankWorkDao;
    @Autowired
    BankOwnerDao bankOwnerDao;
    @Autowired
    UserInfoDao userInfoDao;
    @Autowired
    WorkDao workDao;
    
    
    @Override
    public Result createWorkBank(BankWork bankWork, Long tid) {
        try {
            bankWork.setOwner(tid);
            bankWorkDao.insert(bankWork);
            // 注册到BankOwner表
            if(bankWork.getIsTemp() == 0){
                try{
                    BankOwner bankOwner = bankOwnerDao.selectOne(new LambdaQueryWrapper<BankOwner>().eq(BankOwner::getOid, tid).eq(BankOwner::getBankType, 0));
                    if(bankOwner != null){
                        String bids = bankOwner.getBids();
                        ArrayList<String> arrayList = TypeChange.str2arrl(bids);
                        arrayList.add(bankWork.getBwid().toString());
                        bankOwner.setBids(TypeChange.arrL2str(arrayList));
                        bankOwnerDao.updateById(bankOwner);
                    }else{
                        bankOwner = new BankOwner();
                        bankOwner.setOid(tid);
                        bankOwner.setBids("[" + bankWork.getBwid() + "]");
                        bankOwner.setBankType(0);
                        bankOwnerDao.insert(bankOwner);
                    }
                    return new Result(bankWork.getBwid());
                }catch (Exception e){
                    throw new BusinessException("创建作业库时发生异常",e);
                }
            }else{
                return new Result(bankWork.getBwid());
            }
        }catch (Exception e){
            throw new BusinessException("创建作业库时发生异常",e);

        }
    }

    @Override
    @RoleCheck(role = ProjectRole.TEACHER)
    public Result importWorkBank(Integer bid, Long tid) {
        BankOwner bankOwner = bankOwnerDao.selectById(tid);
        if(bankOwner == null){
            bankOwnerDao.insert(new BankOwner(tid, "[]"));
            bankOwner = bankOwnerDao.selectById(tid);
        }
        String bids = bankOwner.getBids();
        ArrayList<String> arrayList = TypeChange.str2arrl(bids);
        if(!arrayList.contains(bid.toString())){
            arrayList.add(bid.toString());
            bankOwner.setBids(TypeChange.arrL2str(arrayList));
            bankOwnerDao.updateById(bankOwner);
            return new Result("添加成功");
        }else{
            return new Result("您已经添加过这个库啦!");
        }
    }

    @Override
    public Result getWorkBankContent(Long tid, Integer bwid) {
        BankWork bankWork = bankWorkDao.selectById(bwid);
        MyAssert.notNull(bankWork, "id=" + bwid + "的作业库不存在");

        JSONObject jsonObject = new JSONObject();
        UserInfo userInfo = userInfoDao.selectById(bankWork.getOwner());
        Long author = userInfo.getUid();
        jsonObject.put("BankName", bankWork.getBwname());
        jsonObject.put("author", userInfo.getUname());
        jsonObject.put("isPrivate", bankWork.getIsPrivate());
        jsonObject.put("tags", bankWork.getTags());
        jsonObject.put("isMine", author.equals(tid) ?1:0);
        // 统计选择题个数
        JSONArray workCotent = WorkSubmit.getWorkCotentByWBID(bwid);
        JSONObject jo;
        int count_choice = 0;
        int count_fillin = 0;
        int count_text = 0;

        if (workCotent != null) {
            for (int i=0;i<workCotent.size();i++) {
                jo = (JSONObject) workCotent.get(i);
                if (jo.get("qtype").equals(ProjectCode.QueType_choice_question)){
                    count_choice++;
                }else if(jo.get("qtype").equals(ProjectCode.QueType_fillin_question)){
                    count_fillin++;
                } else if (jo.get("qtype").equals(ProjectCode.QueType_text_question)) {
                    count_text++;
                }
            }
        }
        jsonObject.put("numOfQue","[\"" + count_choice +"\",\"" + count_fillin + "\",\"" + count_text+"\"]");
        int usedTimes = 0;
        usedTimes = workDao.selectCount(new LambdaQueryWrapper<Work>().eq(Work::getBwid, bwid));
        jsonObject.put("usedTimes", usedTimes);
        return new Result(jsonObject);
    }

    /**
     * 获取完整的Bank内容
     * */
    @Override
    public Result getWorkBankQuestions(int role, Integer wbid) {
        BankWork bankWork = bankWorkDao.selectById(wbid);
        MyAssert.notNull(bankWork, "作业内容不存在😮");
        try{
            String bakQue;
            if(role>=1){
                bakQue = bankWork.getQuestions();
            }else{
                bakQue = bankWork.getQuestions()
                        .replaceAll(",\\\"cans\":\\\"[^\\\"]*\\\"","")
                        .replaceAll(",\"isCorr\":false","")
                        .replaceAll(",\"isCorr\":true","");
            }
            bankWork.setQuestions(bakQue);
            return new Result(ProjectCode.CODE_SUCCESS,bankWork,"获取成功");
        }catch (Exception e){
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "解析题库时异常", e);
        }
    }

    @Override
    public Result deleteWorkBank(Integer bwid) {
        bankWorkDao.deleteById(bwid);
        return new Result(bankWorkDao.selectById(bwid)==null?"删除成功啦":"QAQ删除好像出了点问题");
    }

    @Override
    public Result editWorksBank(BankWork bankWork) {
        return new Result(bankWorkDao.updateById(bankWork)>0?"修改信息成功啦":"修改时好像出了点问题 ...");
    }

    @Override
    public BankWork getWorkBankById(Integer bwid) {
        return bankWorkDao.selectById(bwid);
    }

    @Override
    public Result getWorkBankByOnwer(Long owner) {
        JSONArray jarr = new JSONArray();
        LambdaQueryWrapper<BankOwner> lqw = new LambdaQueryWrapper<>();
        BankOwner bankOwner = bankOwnerDao.selectOne(lqw.eq(BankOwner::getOid, owner).eq(BankOwner::getBankType, 0));
        MyAssert.notNull(bankOwner,"您还没有创建或导入过作业库哦~");
        try{
            String bids = bankOwner.getBids();
            ArrayList<String> arrayList = TypeChange.str2arrl(bids);
            if(arrayList.size() > 0){
                for (String s : arrayList) {
                    JSONObject o = new JSONObject();
                    BankWork bankWork = bankWorkDao.selectById(Integer.valueOf(s));
                    if(bankWork.getIsTemp() != 1){
                        o.put("id", bankWork.getBwid());
                        o.put("isPrivate", bankWork.getIsPrivate());
                        o.put("bankName", bankWork.getBwname());
                        o.put("tags", bankWork.getTags());
                        o.put("author", bankWork.getOwner());
                        jarr.add(o);
                    }
                }
                return new Result(jarr);
            }else{
                return new Result("[]","没有添加库");
            }
        }catch(Exception e){
            throw new BusinessException("获取作业库时出了些问题 ... ",e);
        }
    }

    @Override
    public Result addBankTags(Integer bwid, ArrayList<String> tags) {
        try {
            BankWork bankWork = bankWorkDao.selectById(bwid);
            ArrayList<String> origin = TypeChange.str2arrl(bankWork.getTags());
            origin.addAll(tags);
            bankWork.setTags(origin.toString());
            bankWorkDao.updateById(bankWork);
        }catch (Exception e){
            throw new BusinessException("为题库添加Tag时出了些问题 ... ",e);
        }
        return new Result("添加成功!");
    }

    @Override
    public Result getMyBankSummary(Long tid) {
        List<BankWork> bankWorks = bankWorkDao.selectList(new LambdaQueryWrapper<BankWork>().eq(BankWork::getOwner, tid).eq(BankWork::getIsTemp, 0));
        ArrayList<JSONObject> ret = new ArrayList<>();
        for (BankWork bankWork : bankWorks) {
            JSONObject jo = new JSONObject();
            jo.put("bwname", bankWork.getBwname());
            jo.put("usageCount", workDao.selectCount(new LambdaQueryWrapper<Work>().eq(Work::getBwid, bankWork.getBwid())));
            ret.add(jo);
        }
        ret.sort((o1, o2) -> o2.getInteger("usageCount") - o1.getInteger("usageCount"));
        return new Result(ret.subList(0,5));
    }
}
