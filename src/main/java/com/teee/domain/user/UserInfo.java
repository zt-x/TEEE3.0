package com.teee.domain.user;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("user_info")
@Data
public class UserInfo {
    @TableId
    @ExcelProperty("学号")
    Long uid;
    @ExcelIgnore
    Integer role;
    @ExcelProperty("姓名")
    String uname;
    @ExcelIgnore
    String avatar;
    @ExcelIgnore
    Integer loginCount;

    public UserInfo(Long uid, Integer role, String username, String avatar) {
        this.uid = uid;
        this.role = role;
        this.uname = username;
        this.avatar = avatar;
    }
    public UserInfo(Long uid, String username, Integer role) {
        this.uid = uid;
        this.uname = username;
        this.role = role;
    }

    public UserInfo(Long uid, String avatar) {
        this.uid = uid;
        this.avatar = avatar;
    }
    public UserInfo(){

    }
}
