package com.teee.service;

import com.teee.vo.Result;


/**
 * @author Xu ZhengTao
 * @version 3.0
 */
public interface AccountService {
    Result register(Long uid, String uname);
    Result login(Long uid, String pwd);
    Result updateUserInfo(Long uid, String uname, String avatar);
    Result getUserInfo(Long uid);
    Result delUser(Long uid);
}
