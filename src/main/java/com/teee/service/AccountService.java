package com.teee.service;

import com.teee.vo.R;


/**
 * @author Xu ZhengTao
 * @version 3.0
 */
public interface AccountService {
    public R register(Long uid, String uname);
    public R login(Long uid, String pwd);
    public R updateUserInfo(Long uid, String uname, String avatar);
    public R getUserInfo(Long uid);
    public R delUser(Long uid);
}
