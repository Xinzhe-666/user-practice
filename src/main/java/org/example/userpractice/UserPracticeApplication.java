package org.example.userpractice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("org.example.userpractice.mapper")
@EnableAsync  // 必须加：开启异步方法支持
public class UserPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserPracticeApplication.class, args);
    }

}