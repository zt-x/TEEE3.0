package com.teee.service.Impl;

import com.teee.service.AccountService;
import com.teee.vo.R;
import org.springframework.stereotype.Service;

/**
 * @author Xu ZhengTao
 * @version 3.0
 */
@Service
public class AccountServiceImpl implements AccountService {

    // TODO

    @Override
    public R register(Long uid, String uname) {
        return null;
    }

    @Override
    public R login(Long uid, String pwd) {
        return null;
    }

    @Override
    public R updateUserInfo(Long uid, String uname, String avatar) {
        return null;
    }

    @Override
    public R getUserInfo(Long uid) {
        return null;
    }

    @Override
    public R delUser(Long uid) {
        return null;
    }
}
