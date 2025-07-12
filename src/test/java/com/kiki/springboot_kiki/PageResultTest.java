package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.Pojo.PageResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class PageResultTest {

    @Test
    public void testGetterSetterAndToString() {
        PageResult<String> pageResult = new PageResult<>();
        
        // 测试所有 getter/setter 方法
        List<String> content = Arrays.asList("item1", "item2", "item3");
        pageResult.setContent(content);
        pageResult.setTotalElements(100);
        pageResult.setTotalPages(10);
        pageResult.setCurrentPage(1);
        pageResult.setPageSize(10);
        pageResult.setHasNext(true);
        pageResult.setHasPrevious(false);
        
        // 验证 getter 方法
        assertEquals(content, pageResult.getContent());
        assertEquals(100, pageResult.getTotalElements());
        assertEquals(10, pageResult.getTotalPages());
        assertEquals(1, pageResult.getCurrentPage());
        assertEquals(10, pageResult.getPageSize());
        assertTrue(pageResult.getHasNext());
        assertFalse(pageResult.getHasPrevious());
        
        // 测试 toString 方法
        String toString = pageResult.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("PageResult"));
        assertTrue(toString.contains("totalElements=100"));
        assertTrue(toString.contains("currentPage=1"));
    }
    
    @Test
    public void testConstructor() {
        List<String> content = Arrays.asList("item1", "item2");
        PageResult<String> pageResult = new PageResult<>(content, 50, 5, 2, 10, true, true);
        
        assertEquals(content, pageResult.getContent());
        assertEquals(50, pageResult.getTotalElements());
        assertEquals(5, pageResult.getTotalPages());
        assertEquals(2, pageResult.getCurrentPage());
        assertEquals(10, pageResult.getPageSize());
        assertTrue(pageResult.getHasNext());
        assertTrue(pageResult.getHasPrevious());
    }
} 