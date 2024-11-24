package com.capstone_design.mobile_forensics;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class GoogleInitializer {
    @Value("${google.cloud.credentials.path}")
    private String credentialsPath;

    @PostConstruct
    public void initGoogleCredentials() {
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", credentialsPath);
        System.out.println("GOOGLE_APPLICATION_CREDENTIALS has been set to: " + credentialsPath);
    }
}

