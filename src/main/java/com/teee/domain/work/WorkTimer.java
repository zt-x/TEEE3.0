package com.teee.domain.work;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Xu ZhengTao
 */
@Data
@TableName("work_timer")
public class WorkTimer {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("restTime")
    private String restTime;

    @TableField("uid")
    private Long uid;

    @TableField("wid")
    private Integer wid;
}
