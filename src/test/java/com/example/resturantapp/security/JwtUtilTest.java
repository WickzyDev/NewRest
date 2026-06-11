package com.example.resturantapp.security;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() throws Exception {
        jwtUtil = new JwtUtil();
        // set private fields via reflection
        Field secretField = JwtUtil.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        // 64+ chars for HMAC key
        secretField.set(jwtUtil, "0123456701234567012345670123456701234567012345670123456701234567");

        Field expField = JwtUtil.class.getDeclaredField("expiration");
        expField.setAccessible(true);
        expField.setLong(jwtUtil, 1000L * 60 * 60); // 1 hour
    }

    @Test
    public void generateAndExtractToken() {
        String email = "user@example.com";
        String role = "ROLE_USER";

        String token = jwtUtil.generateToken(email, role);
        assertNotNull(token);

        String extractedEmail = jwtUtil.extractEmail(token);
        assertEquals(email, extractedEmail);

        String extractedRole = jwtUtil.extractRole(token);
        assertEquals(role, extractedRole);

        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    public void tamperedTokenIsInvalid() {
        String token = jwtUtil.generateToken("a@b.com", "R");
        String tampered = token + "x";
        assertFalse(jwtUtil.validateToken(tampered));
    }
}
