package org.example.userpractice.common;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT令牌工具类
 * 功能：封装令牌生成、合法性校验、用户信息解析能力
 */
@Component // 交给Spring容器管理，其他地方可以直接@Autowired注入使用
public class JwtUtil {

    // 从配置文件中读取JWT签名私钥
    @Value("${jwt.secret}")
    private String jwtSecret;

    // 从配置文件中读取令牌过期时长
    @Value("${jwt.expire-time}")
    private Long expireTime;

    /**
     * 生成JWT令牌
     * @param userId 用户ID
     * @param username 用户名
     * @param role 用户角色
     * @return 生成的JWT令牌字符串
     */
    public String generateToken(Integer userId, String username, String role) {
        // 1. 把配置里的字符串私钥，转换成JWT要求的安全密钥对象
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        // 2. 构建并生成JWT令牌
        return Jwts.builder()
                // 设置令牌主题：用户ID，唯一标识用户
                .setSubject(userId.toString())
                // 自定义字段：用户名
                .claim("username", username)
                // 自定义字段：用户角色（权限控制的核心）
                .claim("role", role)
                // 设置令牌的签发时间
                .setIssuedAt(new Date())
                // 设置令牌的过期时间：当前时间 + 配置的过期时长
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                // 用HS256算法对令牌进行签名，防止篡改
                .signWith(key, SignatureAlgorithm.HS256)
                // 压缩成最终的JWT令牌字符串
                .compact();
    }

    /**
     * 校验令牌是否合法有效
     * @param token 前端传来的JWT令牌
     * @return true=令牌合法有效，false=令牌无效/已过期/被篡改
     */
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            // 解析令牌：如果令牌无效、过期、签名错误、格式不对，会直接抛出异常
            Jwts.parserBuilder()
                    .setSigningKey(key) // 设置签名密钥，用于验证签名
                    .build()
                    .parseClaimsJws(token);
            // 没有抛出异常，说明令牌合法有效
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 令牌异常，返回false
            return false;
        }
    }

    /**
     * 从令牌中解析出用户ID
     */
    public Integer getUserIdFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Integer.parseInt(claims.getSubject());
    }

    /**
     * 从令牌中解析出用户名
     */
    public String getUsernameFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("username", String.class);
    }

    /**
     * 从令牌中解析出用户角色
     */
    public String getRoleFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }
}