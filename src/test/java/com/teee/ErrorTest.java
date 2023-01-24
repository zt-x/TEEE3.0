package com.teee;

import com.teee.utils.MyAssert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ErrorTest {
    @Test
    public void err1(){
        int a;
        String b = null;
        MyAssert.notNull(b,"Empty");
    }
}
