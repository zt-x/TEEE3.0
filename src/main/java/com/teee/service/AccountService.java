package com.teee.service;

import com.alibaba.fastjson.JSONObject;
import com.teee.vo.Result;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * @author Xu ZhengTao
 * @version 3.0
 */
public interface AccountService {
    Result register(JSONObject jo);
    Result login(JSONObject jo);
    Result updateUserInfo(JSONObject jo);
    Result getUserInfo(JSONObject jo);
    Result getBaseUserInfo(String token);
    Result delUser(JSONObject jo);
}
