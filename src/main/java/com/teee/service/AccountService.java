package com.teee.service;

import com.alibaba.fastjson.JSONObject;
import com.teee.vo.Result;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Xu ZhengTao
 * @version 3.0
 */
public interface AccountService {
    Result resetPassword(Long uid, JSONObject jo);
    Result register(JSONObject jo);
    Result login(JSONObject jo);
    Result updateUserInfo(JSONObject jo);
    Result updateUserAvatar(String token, MultipartFile file, HttpServletRequest request);
    Result getUserInfo(Long uid);
    Result getBaseUserInfo(String token);
    Result delUser(JSONObject jo);
}
