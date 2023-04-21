package com.teee.domain.bank;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Xu ZhengTao
 */
@Data
@TableName("bank_owner")
public class BankOwner {

    @TableId("owner_id")
    Long oid;
    @TableField("bids")
    String bids;

    @TableField("bank_type")
    Integer bankType;


    public BankOwner(Long oid, String bids) {
        this.oid = oid;
        this.bids = bids;
    }

    public BankOwner() {
    }
}
