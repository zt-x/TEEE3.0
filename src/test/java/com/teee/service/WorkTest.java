package com.teee.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class WorkTest {
   @Autowired
    WorkService workService;

   @Test
    public void getQue(){
       System.out.println(workService.getQueContent(70, 4));
   }
}
