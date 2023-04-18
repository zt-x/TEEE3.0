package com.teee;

import com.alibaba.fastjson.JSONObject;
import com.teee.controller.Account.AccountController;
import com.teee.utils.MockData;
import com.teee.utils.SpringBeanUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MockDataTest {

    @Test
    void addUser(){
        MockData.addRandomUser();
    }

    @Test
    void addParticularUser(){
        MockData.addParticularUser("11","tea11", 1,"11");
    }

    @Test
    void rpwd(){
        AccountController bean = SpringBeanUtil.getBean(AccountController.class);
        for(int i=0; i<=2; i++){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uid",i);
            jsonObject.put("pwd",i);
//            bean.resetPassword(jsonObject);
        }

    }

}
