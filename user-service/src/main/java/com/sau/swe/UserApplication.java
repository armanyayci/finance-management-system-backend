package com.sau.swe;

import com.sau.swe.utils.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class UserApplication {
    public static void main(String[] args){
        SpringApplication app = new SpringApplication(UserApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", Constants.USER_RUN_PORT));
        app.run(args);
    }
}
