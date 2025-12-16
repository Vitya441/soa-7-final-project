package org.example.authservice;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashGeneratorForAdmin {
    public static void main(String[] args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "22817";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }
}
