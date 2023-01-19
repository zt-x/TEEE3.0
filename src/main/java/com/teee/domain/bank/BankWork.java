package com.teee.domain.bank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Xu ZhengTao
 * @data: questions: JSONArray<JSONObject>.toString();
 */
@Data
@TableName("bank_work")
public class BankWork {
    @TableId(type = IdType.AUTO)
    private Integer bwid;
    private String questions;
    private String bwname;
    private Long owner;
    private String tags;
    private Integer isTemp;
    private Integer isPrivate;
    @TableLogic
    private Integer deleted;

    public BankWork(String workName) {
        this.bwname = workName;
    }

    public BankWork(String questions, String workName, Integer isTemp) {
        this.questions = questions;
        this.bwname = workName;
        this.isTemp = isTemp;
    }

    public BankWork(String workName, Long owner) {
        this.bwname = workName;
        this.owner = owner;
    }

    public BankWork(String workName, String questions, Long owner, String tags, Integer isTemp) {
        this.bwname = workName;
        this.questions = questions;
        this.owner = owner;
        this.tags = tags;
        this.isTemp = isTemp;
    }


    public BankWork() {
    }
}
