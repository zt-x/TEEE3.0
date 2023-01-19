package com.teee.domain.bank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Xu ZhengTao
 * @data{
 *  bankType: 题库类型，1、选择题 2、填空题 3、简答题
 *  questions: JSONArray
 * }
 */
@Data
@TableName("bank_question")
public class BankQuestion {
    @TableId(type = IdType.AUTO)
    private Integer bqid;
    private String bqname;
    private Integer bqType;
    private String questions;
    private Long owner;
    private String tags;
    private Integer isPrivate;
    @TableLogic
    private Integer deleted;

}
