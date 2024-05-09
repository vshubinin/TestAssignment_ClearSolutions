package com.practical.assignment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${user.minimum.age}")
    private int minimumUserAge;

    public int getMinimumUserAge() {
        return minimumUserAge;
    }
}
