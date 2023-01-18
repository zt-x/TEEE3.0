package com.teee.domain.accouncement;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("announcement")
public class Announcement {
    Integer aid;
    String type;
    String title;
    String releaseTime;
    String content;
}
