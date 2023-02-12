package com.teee.service;

import com.alibaba.fastjson.JSONObject;
import com.teee.vo.Result;


/**
 * @author Xu ZhengTao
 * @version 3.0
 */
public interface AccountService {
    Result register(JSONObject jo);
    Result login(JSONObject jo);
    Result updateUserInfo(JSONObject jo);
    Result getUserInfo(Long uid);
    Result getBaseUserInfo(String token);
    Result delUser(JSONObject jo);
}
