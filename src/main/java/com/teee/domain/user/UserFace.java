package com.teee.domain.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("user_face")
@Data
public class UserFace {
    @TableId
    Long uid;
    String faceSrc;

    public UserFace(Long uid, String faceSrc) {
        this.uid = uid;
        this.faceSrc = faceSrc;
    }

    public UserFace() {
    }
}
