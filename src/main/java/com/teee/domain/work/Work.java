package com.teee.domain.work;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("work")
public class Work {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer cid;
    private Integer bwid;
    private String wname;
    private String deadline;
    private Float timeLimit;
    private Integer isExam;
    private Float totalScore;
    private Integer autoReadoverChoice;
    private Integer autoReadoverFillIn;
    private Integer status;
    //@TableField(exist = false)
    //private WorkExamRule rule;

    @TableLogic//逻辑删除
    private Integer deleted;
    @Version
    private Integer version;
}
