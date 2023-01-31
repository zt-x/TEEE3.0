package com.teee;

import com.teee.utils.JWT;
import org.junit.jupiter.api.Test;

public class UtilTest {
    @Test
    public void getToken(){
    }

    @Test
    public void getSecrtKeyToken(){
        String si = "===AKIDjU6ZMxWiookaLzXjrsjPtAIG3hEbopWE===";
        String sk = "666_gHi5LQwKWbTAh3DD2YtAFAvY5RcdVBi3_666";
        System.out.println(JWT.jwtEncryptTencentKey(si, sk));
    }
}
