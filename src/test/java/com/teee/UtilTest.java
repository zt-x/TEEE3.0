package com.teee;

import com.teee.controller.Project.ProjectRole;
import com.teee.util.Jwt;
import org.junit.jupiter.api.Test;

public class UtilTest {
    @Test
    public void getToken(){
        System.out.println("STU Token: " + Jwt.jwtEncrypt(123, ProjectRole.STUDENT));
        System.out.println("TEA Token: " + Jwt.jwtEncrypt(123, ProjectRole.TEACHER));
        System.out.println("ADM Token: " + Jwt.jwtEncrypt(123, ProjectRole.ADMIN));
    }
}
