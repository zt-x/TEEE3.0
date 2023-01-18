package com.teee.domain.work;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Xu ZhengTao
 */
@Data
@TableName("work_exam_track")
public class WorkExamTrack {
    @TableId
    private Long uid;

    @TableId
    private Integer wid;

    private String faceCheck;

    private String ipAddress;

    private String enterTime;

    private Integer closeTimes;

    private String flashPhotos;

    @TableLogic//逻辑删除
    private Integer deleted;
}
