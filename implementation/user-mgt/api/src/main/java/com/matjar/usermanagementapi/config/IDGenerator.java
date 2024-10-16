package com.matjar.usermanagementapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

@Configuration
public class IDGenerator {

    @Value("${service.abbreviation}")
    private String serviceAbbreviation;

    public String getTransactionId() {
        long timestamp = Instant.now().toEpochMilli();
        return serviceAbbreviation + "-" +  timestamp;
    }


}
