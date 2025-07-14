package com.ping.aicodehelper.ai;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AiCodeHelperServiceTest {

    @Resource
    private AiCodeHelperService aiCodeHelperService;

    @Test
    void chat() {
        String result = aiCodeHelperService.chat("你好，我是程序员秋棠");
        System.out.println(result);
    }

    @Test
    void chatWithMemory() {
        String result = aiCodeHelperService.chat("你好，我是程序员秋棠");
        System.out.println(result);
        result = aiCodeHelperService.chat("你好，我是谁来着？");
        System.out.println(result);
    }

    @Test
    void chatForReport() {
        String userMessage = "你好，我是程序员秋棠，学编程两年半，请帮我制定学习报告";
        AiCodeHelperService.Report report = aiCodeHelperService.chatForReport(userMessage);
        System.out.println(report);
    }

    @Test
    void chatWithRag() {
        String result = aiCodeHelperService.chat("怎么学习Java？有哪些常见的面试题？");
        // 验证响应包含文档特定内容
        assertTrue(result.contains("ArrayList"));
        assertTrue(result.contains("LinkedList"));
        assertTrue(result.contains("equals"));
        assertTrue(result.contains("hashCode"));
        System.out.println("RAG响应验证通过：");
        System.out.println(result);
    }
}