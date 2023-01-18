package com.teee.domain.Bank;

import com.baomidou.mybatisplus.annotation.IdType;
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
    @TableId(type = IdType.AUTO)
    Integer id;
    @TableField("owner_id")
    Long oid;
    @TableField("bids")
    String bids;

    @TableField("bank_type")
    Integer bankType;
}
