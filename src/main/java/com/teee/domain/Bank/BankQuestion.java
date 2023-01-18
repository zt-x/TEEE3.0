package com.teee.domain.Bank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
    private Integer bankId;

    private String bankName;
    private Integer bankType;
    private String questions;
    private Long owner;
    private String tags;
    private Integer isPrivate;
    public BankQuestion(String bankName, Integer bankType, String questions, Long owner, String tags) {
        this.bankName = bankName;
        this.bankType = bankType;
        this.questions = questions;
        this.owner = owner;
        this.tags = tags;
    }

    public BankQuestion(String bankName, Integer bankType, String questions, Long owner) {
        this.bankName = bankName;
        this.bankType = bankType;
        this.questions = questions;
        this.owner = owner;
    }
    public BankQuestion(String bankName, Integer bankType,Long owner) {
        this.bankName = bankName;
        this.bankType = bankType;
        this.owner = owner;
    }

    public BankQuestion(Integer bankId, String bankName, Integer bankType, String questions, Long owner, String tags) {
        this.bankId = bankId;
        this.bankName = bankName;
        this.bankType = bankType;
        this.questions = questions;
        this.owner = owner;
        this.tags = tags;
    }
}
