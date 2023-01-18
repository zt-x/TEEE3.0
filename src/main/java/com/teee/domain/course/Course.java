package com.teee.domain.course;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
/**
 * @author Xu ZhengTao
 * 课程的实体类
 */
@Data
public class Course {
    @TableId(type= IdType.AUTO)
    private Integer cid;
    @TableField("name")
    private String cname;
    private Long tid;
    private String works;
    private String exams;
    private String banner;
    private String startTime;
    private String endTime;
    private Integer status;

}
