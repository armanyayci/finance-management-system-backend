package com.sau.swe;

import com.sau.swe.utils.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AuthApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", Constants.AUTH_RUN_PORT));
        app.run(args);
    }
}
