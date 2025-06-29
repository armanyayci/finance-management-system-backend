package com.sau.swe.notification;

import com.sau.swe.utils.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.Collections;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class NotificationApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(NotificationApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", Constants.NOTIFICATION_RUN_PORT));
        app.run(args);
    }
} 