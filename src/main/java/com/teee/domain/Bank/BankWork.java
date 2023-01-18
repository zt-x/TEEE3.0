package com.teee.domain.Bank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Xu ZhengTao
 * @data: questions: JSONArray<JSONObject>.toString();
 */
@Data
@TableName("bank_work")
public class BankWork {
    @TableId(value="work_id", type = IdType.AUTO)
    private Integer workId;
    private String questions;
    private String workName;
    private Long owner;
    private String tags;

    private Integer isTemp;

    private Integer isPrivate;

    public BankWork(String workName) {
        this.workName = workName;
    }

    public BankWork(String questions, String workName, Integer isTemp) {
        this.questions = questions;
        this.workName = workName;
        this.isTemp = isTemp;
    }

    public BankWork(String workName, Long owner) {
        this.workName = workName;
        this.owner = owner;
    }

    public BankWork(String workName, String questions, Long owner, String tags, Integer isTemp) {
        this.workName = workName;
        this.questions = questions;
        this.owner = owner;
        this.tags = tags;
        this.isTemp = isTemp;
    }


    public BankWork() {
    }
}
