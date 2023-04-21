package com.teee.domain.course;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseUserInfo {
    @ExcelProperty("学号")
    private Long uid;
    @ExcelProperty("姓名")
    private String uname;
    @ExcelProperty("完成作业数")
    private Integer count;
    @ExcelProperty("作业平均分")
    private float avg;
    @ExcelProperty("最新一次考试成绩")
    private float lastExamScore;
}
