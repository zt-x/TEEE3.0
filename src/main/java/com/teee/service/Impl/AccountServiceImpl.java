package com.teee.service.Impl;

import com.teee.service.AccountService;
import com.teee.vo.Result;
import org.springframework.stereotype.Service;

/**
 * @author Xu ZhengTao
 * @version 3.0
 */
@Service
public class AccountServiceImpl implements AccountService {

    // TODO

    @Override
    public Result register(Long uid, String uname) {
        return null;
    }

    @Override
    public Result login(Long uid, String pwd) {
        return null;
    }

    @Override
    public Result updateUserInfo(Long uid, String uname, String avatar) {
        return null;
    }

    @Override
    public Result getUserInfo(Long uid) {
        return null;
    }

    @Override
    public Result delUser(Long uid) {
        return null;
    }
}
