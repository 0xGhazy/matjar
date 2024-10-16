package com.matjar.usermanagementapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import java.security.SecureRandom;

@Configuration
public class RandomCodeGeneratorConfig {

    @Value("${activation.code.alphanumeric}")
    private String alphanumeric;
    private static final SecureRandom RANDOM = new SecureRandom();
    @Value("${activation.code.length}")
    private int codeLength;

    public String generateSecureRandomCode() {
        StringBuilder code = new StringBuilder(codeLength);
        for (int i = 0; i < codeLength; i++) {
            int index = RANDOM.nextInt(alphanumeric.length());
            code.append(alphanumeric.charAt(index));
        }
        return code.toString();
    }
}
