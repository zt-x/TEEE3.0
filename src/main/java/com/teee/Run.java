package com.teee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Xu ZhengTao
 * @version 3.0
 */
@SpringBootApplication
@EnableAsync
public class Run {
    public static void main(String[] args) {
        SpringApplication.run(Run.class, args);
    }
}
