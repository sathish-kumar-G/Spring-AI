package com.spring.ai.spring_ai_demo.rag.controller.config;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class RagConfiguration {


    @Value("vectorstore.json")
    private String vectorStoreName;

    @Value("classpath:/docs/olympic-faq.txt")
    Resource faq;

    @Bean
    SimpleVectorStore simpleVectorStore(EmbeddingClient embeddingClient) {

        SimpleVectorStore simpleVectorStore = new SimpleVectorStore(embeddingClient);

        File vectorStoreFile = getVectorStoreFile();
        if (vectorStoreFile.exists()) {
            simpleVectorStore.load(vectorStoreFile);
        } else {
            TextReader textReader = new TextReader(faq);
            textReader.getCustomMetadata().put("fileName", "olympic-faq.txt");
            List<Document> documents = textReader.get();
            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> splitDocuments = textSplitter.apply(documents);
            simpleVectorStore.add(splitDocuments);
            simpleVectorStore.save(vectorStoreFile);
        }

        return simpleVectorStore;

    }

    private File getVectorStoreFile() {
        Path path = Paths.get("src", "main", "resources", "data");
        String absolutePath = path.toFile().getAbsoluteFile() + "/" + vectorStoreName;
        return new File(absolutePath);
    }
}
