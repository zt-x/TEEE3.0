package com.teee.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.teee.dao.KeyTimerDao;
import com.teee.domain.Key;
import com.teee.project.ProjectCode;
import com.teee.service.CourseService;
import com.teee.service.KeyService;
import com.teee.service.WorkBankService;
import com.teee.utils.JWT;
import com.teee.utils.MyAssert;
import com.teee.vo.Result;
import com.teee.vo.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author Xu ZhengTao
 */
@Service
public class KeyServiceImpl implements KeyService {

    @Autowired
    KeyTimerDao keyTimerDao;
    @Autowired
    CourseService courseService;
    @Autowired
    WorkBankService workBankService;

    @Override
    public Result createKey(String token, Key key) {
        try{
            key.setKeyId(String.valueOf(Integer.parseInt(key.getRestTime())));
        }catch (Exception e){
            key.setRestTime("无限制");
        }
        // 去重复
        String randomKey = getRandomString2(16);
        while(keyTimerDao.selectCount(new LambdaQueryWrapper<Key>().eq(Key::getKeyId, randomKey))>0){
            randomKey = getRandomString2(16);
        }
        key.setKeyId(randomKey);
        int k = keyTimerDao.insert(key);
        return new Result(key.getKeyId(), "创建Key" + (k>0?"成功!":"失败QAQ ..."));
    }

    @Override
    public Result deleteKey(String token,String key) {
        return null;
    }

    @Override
    public Result useKey(String token, String key) {
        Key keyObj = keyTimerDao.selectById(key);
        MyAssert.notNull(keyObj, "😖 您输入的key不存在哦~");
        if(!"无限制".equals(keyObj.getRestTime()) && Integer.parseInt(keyObj.getRestTime()) <0){
            throw new BusinessException("⏱ 这个Key已经过期啦!");
        }
        // TODO 0 添加Key的用途
        if(keyObj.getAction().equals(ProjectCode.KEY_INTER_COURSE)){
            JSONObject jo = new JSONObject();
            jo.put("cid", keyObj.getParam());
            return courseService.addCourse(token, jo);
        }else if(keyObj.getAction().equals(ProjectCode.KEY_IMPORT_WORKBANK)){
            return workBankService.importWorkBank( Integer.parseInt(keyObj.getParam()), JWT.getUid(token));
        }
        return new Result("⏱ 这个Key对应的方法已经过期啦!");
    }

    /**
     * @author https://blog.csdn.net/cpa_821/article/details/85054198
     * */
    public static String getRandomString2(int length){
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<length;i++){
            int number=random.nextInt(3);
            long result=0;
            switch(number){
                case 0:
                    result=Math.round(Math.random()*25+65);
                    sb.append(String.valueOf((char)result));
                    break;
                case 1:
                    result=Math.round(Math.random()*25+97);
                    sb.append(String.valueOf((char)result));
                    break;
                case 2:
                    sb.append(String.valueOf(new Random().nextInt(10)));
                    break;
                default:
                    break;
            }
        }
        return sb.toString();
    }
}
