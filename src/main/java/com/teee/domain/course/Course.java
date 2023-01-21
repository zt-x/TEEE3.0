package com.teee.domain.course;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
/**
 * @author Xu ZhengTao
 * 课程的实体类
 */
@Data
public class Course {
    @TableId(type= IdType.AUTO)
    private Integer cid = -1;
    private String cname = "default name";
    private Long tid = -1L;
    private String college = "";
    private String banner = "";
    private String works = "[]";
    private String exams = "[]";
    private String startTime = "1000-01-01";
    private String endTime = "9999-12-31";
    private Integer status = 1;

    @TableLogic
    private Integer deleted = 0;

}
