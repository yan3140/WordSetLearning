package com.fd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.fd.mapper")
public class WordSetLearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordSetLearningApplication.class, args);
    }

}
