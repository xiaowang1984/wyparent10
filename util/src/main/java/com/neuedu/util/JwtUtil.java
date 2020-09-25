package com.neuedu.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtUtil {
    static final String KEY = "token";
    private Integer userid;
    private String pwd;
    private Date timestamp;
    private static String createToken(Integer id,String password,Date expires) {
        return JWT.create()
                .withClaim("userid",id)
                .withClaim("pwd",password)
                .withExpiresAt(expires)
                .sign(Algorithm.HMAC256(KEY));
    }
    public static String createTokenBySession(Integer id,String password) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,30);
        return createToken(id,password,calendar.getTime());
    }
    public static String createTokenByQuery(Integer id,String password) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,5);
        return createToken(id,password,calendar.getTime());
    }
    public static JwtUtil deToken(String tokenstring) {
        DecodedJWT decode = JWT.decode(tokenstring);
        Integer userid = decode.getClaim("userid").asInt();
        String pwd = decode.getClaim("pwd").asString();
        Date date = decode.getExpiresAt();
        return new JwtUtil(userid,pwd,date);
    }
}
