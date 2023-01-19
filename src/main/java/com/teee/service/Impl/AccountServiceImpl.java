package com.teee.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.api.R;
import com.teee.dao.UserInfoDao;
import com.teee.dao.UserLoginDao;
import com.teee.domain.user.UserInfo;
import com.teee.domain.user.UserLogin;
import com.teee.exception.BusinessException;
import com.teee.project.ProjectCode;
import com.teee.service.AccountService;
import com.teee.util.JWT;
import com.teee.util.MyAssert;
import com.teee.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.charset.StandardCharsets;

/**
 * @author Xu ZhengTao
 * @version 3.0
 */
@Service
public class AccountServiceImpl implements AccountService {

    // TODO
    @Autowired
    UserLoginDao userLoginDao;

    @Autowired
    UserInfoDao userInfoDao;


    @Override
    public Result register(JSONObject jo) {
        Long uid = jo.getLong("uid");
        String pwd = jo.getString("pwd");
        String uname = jo.getString("uname");
        Integer role = jo.getInteger("role");
        MyAssert.notNull(uid,"请输入用户学号/ID");
        MyAssert.notNull(pwd,"请输入密码!");
        MyAssert.notNull(uname,"请输入姓名!");
        MyAssert.notNull(role,"请选择用户的角色!");
        pwd = DigestUtils.md5DigestAsHex(pwd.getBytes(StandardCharsets.UTF_8));
        MyAssert.isTrue(
                userLoginDao.insert(new UserLogin(uid, pwd, role)) == 1
                            && userInfoDao.insert(new UserInfo(uid,uname,role)) == 1,
                "注册失败"
        );
        return new Result(ProjectCode.CODE_SUCCESS, "注册成功！");
    }

    @Override
    public Result login(JSONObject jo) {
        Long uid = jo.getLong("uid");
        String pwd = jo.getString("pwd");
        MyAssert.notNull(uid,"参数异常，未读取到账号");
        MyAssert.notNull(pwd,"参数异常，未读取到密码");
        pwd = DigestUtils.md5DigestAsHex(pwd.getBytes(StandardCharsets.UTF_8));
        UserLogin userLogin = userLoginDao.selectById(uid);
        MyAssert.notNull(userLogin,"未找到用户信息");
        if(pwd.equals(userLogin.getPwd())){
            return new Result(ProjectCode.CODE_SUCCESS, JWT.jwtEncrypt(uid, userLogin.getRole()),"登陆成功");
        }else{
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "密码错误");
        }
    }

    @Override
    public Result updateUserInfo(JSONObject jo) {
        Long uid = jo.getLong("uid");
        String avatar = jo.getString("avatar");
        MyAssert.notNull(uid, "uid参数丢失");
        MyAssert.notNull(avatar, "头像参数丢失");
        if(userInfoDao.selectById(uid) == null){
            return new Result(ProjectCode.CODE_EXCEPTION_TOKENILLEGAL, null, "用户不存在!");
        }else {
            userInfoDao.updateById(new UserInfo(uid, avatar));
        }
        return new Result(ProjectCode.CODE_SUCCESS, null, "编辑资料成功!");
    }
    @Override
    public Result getUserInfo(JSONObject jo) {
        return new Result(ProjectCode.CODE_SUCCESS,userInfoDao.selectById(jo.getLong("uid")),"查询成功");
    }

    @Override
    public Result delUser(JSONObject jo) {
        int res = userLoginDao.deleteById(jo.getLong("uid"));
        if (res>0) {
            return new Result(ProjectCode.CODE_SUCCESS,"删除成功!");
        }else if(res == 0){
            return new Result(ProjectCode.CODE_EXCEPTION_BUSSINESS,"该用户已被删除！");
        }else{
            throw new BusinessException(ProjectCode.CODE_EXCEPTION_BUSSINESS, "啊喔，删除时发生了错误 ...");
        }
    }
}
