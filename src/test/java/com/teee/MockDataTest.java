package com.teee;

import com.alibaba.fastjson.JSONObject;
import com.teee.service.AccountService;
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
        MockData.addParticularUser("0","管理员", 2,"Admin@123456!");
    }

    @Test
    void rpwd(){
        AccountService bean = SpringBeanUtil.getBean(AccountService.class);
//        for(int i=0; i<=2; i++){
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("uid",i);
//            jsonObject.put("pwd",i);
////            bean.resetPassword(jsonObject);
//        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uid",1l);
        jsonObject.put("pwd",1);
        bean.resetPassword(jsonObject.getLong("uid"),jsonObject);

    }

}
