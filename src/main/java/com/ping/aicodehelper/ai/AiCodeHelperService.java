package com.ping.aicodehelper.ai;

import com.ping.aicodehelper.ai.guardrail.SafeInputGuardrail;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.guardrail.InputGuardrails;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

import java.util.List;

@InputGuardrails(SafeInputGuardrail.class)
public interface AiCodeHelperService {

    @SystemMessage(fromResource = "system-prompt.txt")
    String chat( String userMessage);

    @SystemMessage(fromResource = "system-prompt.txt")
    Report chatForReport(String userMessage);

    //让Ai生成一个学习报告，ai输出用户名称以及给用户一系列的学习建议
    //学习报告
    record Report(String name, List<String> suggestionList) {

    }

    // 返回封装后的结果
    @SystemMessage(fromResource = "system-prompt.txt")
    Result<String> chatWithRag(String userMessage);


    // 流式输出
    @SystemMessage(fromResource = "system-prompt.txt")
    Flux<String> chatStream(@MemoryId int memoryId, @UserMessage String userMessage);
}
