package com.teee;

import com.teee.utils.MockData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MockDataTest {

    @Test
    void addUser(){
        MockData.addRandomUser();
    }
}
