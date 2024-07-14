package com.spring.ai.spring_ai_demo.rag.controller;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RagFaqController {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Value("classpath:/prompts/rag-prompt-template.st")
    Resource resource;

    public RagFaqController(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }


    @GetMapping("/rag")
    public String demoRag(@RequestParam(name = "message", defaultValue = "How many athletes compete in the Olympic Games Paris 2024") String message) {
        List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(message).withTopK(2));
        List<String> contentList = similarDocuments.stream().map(document -> document.getContent()).toList();
        PromptTemplate promptTemplate = new PromptTemplate(resource);
        Map<String,Object> map = new HashMap<>();
        map.put("input",message);
        map.put("documents",String.join("\n",contentList));
        Prompt prompt = promptTemplate.create(map);
        return chatClient.call(prompt).getResult().getOutput().getContent();

    }
}
