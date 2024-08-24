package com.matjar.user.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.UUID;

@Component
public class RandomIdGenerator {

    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }


    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String getRandomToken(int length) {
        StringBuilder randomString = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            randomString.append(CHARACTERS.charAt(index));
        }
        return randomString.toString();
    }
}
