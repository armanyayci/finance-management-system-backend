package com.sau.swe;


import com.sau.swe.utils.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class AccountApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AccountApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", Constants.ACCOUNT_RUN_PORT));
        app.run(args);
    }
}
