package com.teee.controller;

import com.alibaba.fastjson.JSONObject;
import com.teee.domain.Key;
import com.teee.service.KeyService;
import com.teee.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/keys")
public class KeyController {

    @Autowired
    KeyService keyService;

    @PostMapping
    Result createKey(@RequestHeader("Authorization") String token, @RequestBody Key key){
        return keyService.createKey(token, key);
    }
    @DeleteMapping
    Result delKey(@RequestHeader("Authorization") String token,@RequestBody JSONObject key){
        return keyService.deleteKey(token, key.getString("key"));
    }
    @PostMapping("/use")
    Result useKey(@RequestHeader("Authorization") String token, @RequestBody JSONObject key){
        return keyService.useKey(token,  key.getString("key"));
    }
}
