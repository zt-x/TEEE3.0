package com.teee.domain.course;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("course_user")
public class CourseUser {
    @TableId
    Integer cid;
    String uid;

    public CourseUser(Integer cid, String uid) {
        this.cid = cid;
        this.uid = uid;
    }
}
