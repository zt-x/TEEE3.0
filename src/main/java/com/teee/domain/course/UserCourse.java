package com.teee.domain.course;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_course")
public class UserCourse {
    @TableId
    Long uid;
    String cid;

    public UserCourse(Long uid, String cid) {
        this.cid = cid;
        this.uid = uid;
    }
}
