package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.util.PasswordUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilTest {
    @Test
    public void testEncodeAndMatches() {
        String raw = "123456";
        String encoded = PasswordUtil.encode(raw);
        assertNotNull(encoded);
        assertTrue(PasswordUtil.matches(raw, encoded));
        assertFalse(PasswordUtil.matches("wrong", encoded));
    }
} 