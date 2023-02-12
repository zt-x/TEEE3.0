package com.teee.utils;


import com.teee.project.ProjectCode;
import com.teee.project.ProjectRole;
import com.teee.vo.exception.SystemException;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.UUID;

/**
 * @author Xu ZhengTao
 * @version 3.0
 * 功能：
 *  生成token：jwtEncrypt(long uid, String role)
 *  解析token:
 *      getUid(String token)
 *      getRole(String token)
 * */

public class JWT {
    private static final String SIGNATURE = "Xu ZhengTao";
    private static final long TIME = 1000*60*60*24;
    
    public static String jwtEncryptTencentKey(String secretId, String secretKey){
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwtToken = jwtBuilder
                // header
                .setHeaderParam("type","JWT")
                .setHeaderParam("alg", "HS256")
                // payload
                .claim("secretId", secretId)
                .claim("secretKey", secretKey)
                .setExpiration(new Date(System.currentTimeMillis()+TIME))
                .setId(UUID.randomUUID().toString())
                //Signature
                .signWith(SignatureAlgorithm.HS256, SIGNATURE)
                .compact();
        return jwtToken;
    }
    public static String jwtEncrypt(long uid, int role){
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwtToken = jwtBuilder
                // header
                .setHeaderParam("type","JWT")
                .setHeaderParam("alg", "HS256")
                // payload
                .claim("uid", uid)
                .claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + TIME))
                .setId(UUID.randomUUID().toString())
                //Signature
                .signWith(SignatureAlgorithm.HS256, SIGNATURE)
                .compact();
        return jwtToken;
    }
    public static Claims parse(String token){
        try{
            JwtParser jwtParser = Jwts.parser();
            Jws<Claims> claimsJws = jwtParser.setSigningKey(SIGNATURE).parseClaimsJws(token);
            return claimsJws.getBody();
        }catch (Exception e){
            throw new SystemException(ProjectCode.CODE_EXCEPTION_SYSTEM, "您的登录状态过期啦, 请重新登陆哦",e);
        }

    }
    public static Long getUid(String token){
            return Long.valueOf(String.valueOf(parse(token).get("uid")));
    }
    public static int getRole(String token){
        return Integer.parseInt(String.valueOf(parse(token).get("role")));
    }
    public static boolean isStudent(String token){
        return getRole(token) == ProjectRole.STUDENT.ordinal();
    }
    public static boolean isTeacher(String token){
        return getRole(token) == ProjectRole.TEACHER.ordinal();
    }
    public static boolean isAdmin(String token){
        return getRole(token) == ProjectRole.ADMIN.ordinal();
    }
}


