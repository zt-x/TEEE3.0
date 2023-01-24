package com.teee.domain.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("user_info")
@Data
public class UserInfo {
    @TableId
    Long uid;
    Integer role;
    String uname;
    String avatar;

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
