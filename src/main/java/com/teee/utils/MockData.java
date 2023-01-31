package com.teee.utils;

import com.alibaba.fastjson.JSONObject;
import com.teee.controller.Account.AccountController;

import java.util.Random;

public class MockData {
    public static String[] Years = {"2020","2019","2018","2021","2022"};
    public static String[] lastName = {"许","鲁","谢","孙","王","李","付","何","张","刘","陈","杨","徐","欧阳"};
    public static String[] midName = {"正","午","倩","景","宇","航","子","明","国","筱","雅","婧","泽","或","涛","韬","依","菲","力","合","洋"};
    public static Integer[] identities = {1,0};


    public static void addRandomUser(){
        AccountController bean = SpringBeanUtil.getBean(AccountController.class);
        for (int i = 0; i < 100; i++) {
            bean.register(getRandomUser(0.05f));
        }

    }
    public static JSONObject getRandomUser(float presentOfTeaAndStu){
        JSONObject user = new JSONObject();
        Random rd = new Random();
        String name="";
        name = name + lastName[rd.nextInt(lastName.length)] + (rd.nextInt(2)==0?midName[rd.nextInt(midName.length)]:(midName[rd.nextInt(midName.length)]+midName[rd.nextInt(midName.length)]));
        user.put("uname", name);
        //ERR 此处会出现重复错误
        String uid;
        uid = Years[rd.nextInt(Years.length)] + (1000+rd.nextInt(8999)) + (1000+rd.nextInt(8999));
        user.put("uid", Long.valueOf(uid));

        Integer role;
        role = identities[
                rd.nextInt((int) (presentOfTeaAndStu*10000) + 10000)<(int) (presentOfTeaAndStu*10000)?0:1
                ];

        user.put("role", role);
        user.put("pwd", "123");
        return user;

    }
}
