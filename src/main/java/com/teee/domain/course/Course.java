package com.teee.domain.course;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
    private Integer cid;
    private String cname;
    private Long tid;
    private String college;
    private String banner;
    private String works;
    private String exams;
    private String startTime;
    private String endTime;
    private Integer status;

    @TableLogic
    private Integer deleted;

}
