package com.teee.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class CourseServiceTest {
    @Autowired
    CourseService courseService;


    @Test
    public void getWorks(){
        log.info(courseService.getWorks(28).toString());
    }
}
