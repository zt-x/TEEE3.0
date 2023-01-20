package com.teee.domain.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("user_login")
@Data
public class UserLogin {
    @TableId
    Long uid;
    String pwd;
    Integer role;
    public UserLogin(Long uid, String pwd, Integer role) {
        this.uid = uid;
        this.pwd = pwd;
        this.role = role;
    }
}
