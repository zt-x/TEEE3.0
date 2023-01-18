package com.teee.util;


import com.teee.project.ProjectRole;
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

public class Jwt {
    private static String signature = "Xu ZhengTao";
    private static long time = 1000*60*60*24;
    public static String jwtEncrypt(long uid, ProjectRole role){
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwtToken = jwtBuilder
                // header
                .setHeaderParam("type","JWT")
                .setHeaderParam("alg", "HS256")
                // payload
                .claim("uid", uid)
                .claim("role", role.ordinal())
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .setId(UUID.randomUUID().toString())
                //Signature
                .signWith(SignatureAlgorithm.HS256, signature)
                .compact();
        return jwtToken;
    }
    public static Claims parse(String token){
        JwtParser jwtParser = Jwts.parser();
        Jws<Claims> claimsJws = jwtParser.setSigningKey(signature).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return body;
    }
    public static Long getUid(String token){
        return Long.valueOf(String.valueOf(parse(token).get("uid")));
    }
    public static int getRole(String token){
        return Integer.parseInt(String.valueOf(parse(token).get("role")));
    }
}


