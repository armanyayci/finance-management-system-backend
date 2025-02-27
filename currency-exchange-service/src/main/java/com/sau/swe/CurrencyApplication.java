package com.sau.swe;


import com.sau.swe.utils.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class CurrencyApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CurrencyApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", Constants.CURRENCY_EXCHANGE_RUN_PORT));
        app.run(args);
    }}
