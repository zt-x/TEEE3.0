package com.teee.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Xu ZhengTao
 * @version 3.0
 */
@Data
@TableName("key_timer")
public class Key {
    @TableId("key_id")
    String keyId;
    Integer action;
    String param;
    String restTime;
}
