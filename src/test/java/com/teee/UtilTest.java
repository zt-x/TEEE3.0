package com.teee;

import com.teee.project.ProjectRole;
import com.teee.util.JWT;
import org.junit.jupiter.api.Test;

public class UtilTest {
    @Test
    public void getToken(){
        System.out.println("STU Token: " + JWT.jwtEncrypt(123, ProjectRole.STUDENT.ordinal()));
        System.out.println("TEA Token: " + JWT.jwtEncrypt(123, ProjectRole.TEACHER.ordinal()));
        System.out.println("ADM Token: " + JWT.jwtEncrypt(123, ProjectRole.ADMIN.ordinal()));
    }
}
