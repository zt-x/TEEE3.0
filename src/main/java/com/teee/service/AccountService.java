package com.teee.service;

import com.teee.vo.Result;


/**
 * @author Xu ZhengTao
 * @version 3.0
 */
public interface AccountService {
    public Result register(Long uid, String uname);
    public Result login(Long uid, String pwd);
    public Result updateUserInfo(Long uid, String uname, String avatar);
    public Result getUserInfo(Long uid);
    public Result delUser(Long uid);
}
