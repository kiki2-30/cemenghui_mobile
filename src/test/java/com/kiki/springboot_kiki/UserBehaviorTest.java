package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.Pojo.UserBehavior;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;

public class UserBehaviorTest {

    @Test
    public void testGetterSetterAndToString() {
        UserBehavior behavior = new UserBehavior();
        
        // 测试所有 getter/setter 方法
        behavior.setBehaviorId(1);
        behavior.setUserId(2);
        behavior.setBehaviorType("VIEW");
        behavior.setTargetType("NEWS");
        behavior.setTargetId(3);
        behavior.setIpAddress("192.168.1.1");
        behavior.setUserAgent("Mozilla/5.0");
        behavior.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        
        // 验证 getter 方法
        assertEquals(1, behavior.getBehaviorId());
        assertEquals(2, behavior.getUserId());
        assertEquals("VIEW", behavior.getBehaviorType());
        assertEquals("NEWS", behavior.getTargetType());
        assertEquals(3, behavior.getTargetId());
        assertEquals("192.168.1.1", behavior.getIpAddress());
        assertEquals("Mozilla/5.0", behavior.getUserAgent());
        assertNotNull(behavior.getCreatedTime());
        
        // 测试 toString 方法
        String toString = behavior.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("UserBehavior"));
        assertTrue(toString.contains("behaviorId=1"));
        assertTrue(toString.contains("behaviorType='VIEW'"));
    }
} 