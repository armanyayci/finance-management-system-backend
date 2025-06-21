package com.sau.swe.discoveryserver;


import com.sau.swe.utils.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class DiscoveryServerApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DiscoveryServerApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", Constants.DISCOVERY_SERVER_RUN_PORT));
        app.run(args);
    }

}
