package com.zja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpringbootWebsocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootWebsocketApplication.class, args);
        //没有报错，可以使用github-util jar中的类
//        AbcEntity abcEntity = new AbcEntity();
    }
}
