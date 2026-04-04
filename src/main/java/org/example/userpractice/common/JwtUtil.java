package org.example.userpractice.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT工具类：生成、解析、验证token
 */
@Component
public class JwtUtil {

    /**
     * JWT加密密钥
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Token过期时间：默认24小时（86400000毫秒）
     */
    @Value("${jwt.expire}")
    private long expire;

    /**
     * 生成JWT Token
     * @param userId 用户ID
     * @param username 用户名
     * @param role 用户角色
     * @return 生成的token
     */
    public String generateToken(Integer userId, String username, String role) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expire);

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("username", username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * 解析Token，获取Claims（载荷内容）
     * @param token 前端传过来的token
     * @return 解析后的Claims，解析失败返回null
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // Token过期、篡改、格式错误都会解析失败
            return null;
        }
    }

    /**
     * 【拦截器需要】验证Token是否合法且未过期
     * @param token 前端传过来的token
     * @return true=合法有效，false=无效/过期
     */
    public boolean validateToken(String token) {
        Claims claims = parseToken(token);
        // 解析成功 且 未过期，才是有效token
        return claims != null && !isTokenExpired(claims);
    }

    /**
     * 【拦截器需要】从Token中提取用户ID
     * @param token 前端传过来的token
     * @return 用户ID，解析失败返回null
     */
    public Integer getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return null;
        }
        try {
            return Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断Token是否过期
     * @param claims 解析后的载荷
     * @return true=过期，false=未过期
     */
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}