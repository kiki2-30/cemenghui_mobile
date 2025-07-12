package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.Pojo.ConferenceCategory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;

public class ConferenceCategoryTest {

    @Test
    public void testGetterSetterAndToString() {
        ConferenceCategory category = new ConferenceCategory();
        
        // 测试所有 getter/setter 方法
        category.setCategoryId(1);
        category.setCategoryName("技术会议");
        category.setCategoryDesc("技术相关会议");
        category.setSortOrder(10);
        category.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        
        // 验证 getter 方法
        assertEquals(1, category.getCategoryId());
        assertEquals("技术会议", category.getCategoryName());
        assertEquals("技术相关会议", category.getCategoryDesc());
        assertEquals(10, category.getSortOrder());
        assertNotNull(category.getCreatedTime());
        
        // 测试 toString 方法
        String toString = category.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("ConferenceCategory"));
        assertTrue(toString.contains("categoryId=1"));
        assertTrue(toString.contains("categoryName='技术会议'"));
    }
} 