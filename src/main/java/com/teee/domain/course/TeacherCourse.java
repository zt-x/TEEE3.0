package com.teee.domain.course;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("teacher_course")
public class TeacherCourse {
    Long tid;
    int cid;
}
