package com.ping.aicodehelper.ai.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 加载 RAG
 */
@Slf4j
@Configuration
public class RagConfig {

    @Resource
    private EmbeddingModel qwenEmbeddingModel;

    @Resource
    private EmbeddingStore<TextSegment> embeddingStore;

    @Bean
    public ContentRetriever contentRetriever() {
        // 添加开始日志
//        log.info("开始加载文档...");

        // 1. 加载文档
        List<Document> documents = FileSystemDocumentLoader.loadDocuments("src/main/resources/docs");
//        log.info("加载了 {} 个文档", documents.size());
//
//        if (documents.isEmpty()) {
//            log.warn("没有找到任何文档！请检查路径: src/main/resources/docs");
//        }

        // 2. 文档切割：将每个文档按每段进行分割
        DocumentByParagraphSplitter documentByParagraphSplitter =
                new DocumentByParagraphSplitter(1000, 200);

        // 先分割文档并获取片段列表
//        List<TextSegment> segments = paragraphSplitter.splitAll(documents);
//        log.info("文档分割为 {} 个片段", segments.size());

        // 3. 自定义文档加载器
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(documentByParagraphSplitter)
                // 为了提高搜索质量，为每个 TextSegment 添加文档名称
                .textSegmentTransformer(textSegment ->
                    TextSegment.from(textSegment.metadata().getString("file_name") +
                            "\n" + textSegment.text(),textSegment.metadata()))


                // 使用指定的向量模型
                .embeddingModel(qwenEmbeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        // 加载文档
        ingestor.ingest(documents);

        // 4. 自定义内容查询器
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(qwenEmbeddingModel)
                .maxResults(5) // 最多 5 个检索结果
                .minScore(0.55) // 过滤掉分数小于 0.75 的结果
                .build();

        log.info("RAG 配置完成，内容检索器已创建");
        return contentRetriever;
    }
}
