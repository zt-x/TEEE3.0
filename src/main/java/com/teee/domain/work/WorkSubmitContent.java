package com.teee.domain.work;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Xu ZhengTao
 */
@Data
@TableName("work_submit_content")
public class WorkSubmitContent {
    @TableId(type =  IdType.AUTO)
    private Integer sid;
    /** ["","",""]*/
    private String submitContent;
    /** [{qscore:, qreadover:}]**/
    private String readover;
    private Integer finishReadOver;
    private String files;

}
