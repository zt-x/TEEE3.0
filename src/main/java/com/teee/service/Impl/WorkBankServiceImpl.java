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
import com.teee.project.ProjectCode;
import com.teee.service.WorkBankService;
import com.teee.utils.MyAssert;
import com.teee.utils.TypeChange;
import com.teee.vo.Result;
import com.teee.vo.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


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
        try{
            BankOwner bankOwner = bankOwnerDao.selectOne(lqw.eq(BankOwner::getOid, owner).eq(BankOwner::getBankType, 0));
            MyAssert.notNull(bankOwner,"BankOwner不存在, tid=" + owner);
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
                throw new BusinessException("列表为空");
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
    //
    //@Override
    //public Float readOverWork(BankWork standardBankWork, WorkSubmit submitWork) {
    //    return null;
    //}
}
