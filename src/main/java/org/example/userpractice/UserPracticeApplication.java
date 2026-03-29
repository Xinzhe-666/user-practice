package org.example.userpractice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 扫描mapper包下的所有Mapper接口，注意路径是 org.example.userpractice.mapper
@MapperScan("org.example.userpractice.mapper")
@SpringBootApplication
public class UserPracticeApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserPracticeApplication.class, args);
    }
}