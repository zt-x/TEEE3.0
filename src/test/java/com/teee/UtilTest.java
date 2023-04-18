package com.teee;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.fastjson.JSON;
import com.teee.domain.user.UserInfo;
import com.teee.utils.JWT;
import com.teee.utils.MyAssert;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Slf4j

public class UtilTest {

    @Test
    public void getToken(){
    }

    @Test
    public void getSecrtKeyToken(){
        String si = "===AKIDjU6ZMxWiookaLzXjrsjPtAIG3hEbopWE===";
        String sk = "666_gHi5LQwKWbTAh3DD2YtAFAvY5RcdVBi3_666";
        System.out.println(JWT.jwtEncryptTencentKey(si, sk));
    }


    String pathOfFile = "D:/testTemp/";
    @Test
    public void buildExcel() throws IOException {
        File newMkdir = new File(pathOfFile);
        if(!newMkdir.exists()){
            boolean mkdirs = newMkdir.mkdirs();
            MyAssert.isTrue(mkdirs, "创建文件夹失败了 ... ");
        }
        String file = pathOfFile + System.currentTimeMillis() + ".xlsx";
        System.out.println(file);
        new File(file).createNewFile();

        EasyExcel.write(file, UserInfo.class)
                .sheet("模板").doWrite(data());
    }
    @Test
    public void readExcel(){
        String file = pathOfFile + System.currentTimeMillis() + ".xlsx";

        EasyExcel.read("D:\\testTemp\\1681810913539.xlsx", UserInfo.class,new PageReadListener<UserInfo>(dataList -> {
            for (UserInfo demoData : dataList) {
                log.info("读取到一条数据{}", JSON.toJSONString(demoData));
            }
        })).sheet().doRead();
    }
    private List<UserInfo> data(){
        List<UserInfo> userList = new ArrayList<>();
        for(int i=0; i<10; i++){
            userList.add(new UserInfo((long) i,-1,"name="+i,""));
        }
        return userList;
    }


}
