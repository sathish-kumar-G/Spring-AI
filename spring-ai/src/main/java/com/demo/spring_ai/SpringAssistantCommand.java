package com.demo.spring_ai;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.core.io.Resource;
import org.springframework.shell.command.annotation.Command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command
public class SpringAssistantCommand {


    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    @Value("classpath:/prompts/ocpp-reference.st")
    private Resource sbPromptTemplate;

    public SpringAssistantCommand(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }


    @Command(command = "q")
    public String question(@DefaultValue(value = "What is OCPP?")String question){
        List<Document> documents = vectorStore.similaritySearch(SearchRequest.query(question).withTopK(3));
        List<String> similarDocuments = documents.stream().map(document -> document.getContent()).toList();

        PromptTemplate promptTemplate = new PromptTemplate(sbPromptTemplate);
        Map<String,Object> map = new HashMap<>();
        map.put("input",question);
        map.put("documents",String.join("\n",similarDocuments));
        Prompt prompt = promptTemplate.create(map);
       return chatClient.call(prompt).getResult().getOutput().getContent();
    }


}
