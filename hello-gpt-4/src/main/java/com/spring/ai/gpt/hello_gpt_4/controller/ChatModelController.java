package com.spring.ai.gpt.hello_gpt_4.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ChatModelController {

    private final ChatClient chatClient;

    public ChatModelController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.defaultSystem("you are loud assistant for responsible for all capital letters").build();
    }

    @GetMapping("/dad-jokes")
    public String jokes(@RequestParam(value = "topic", defaultValue = "Tell me a dad joke") String topic) {
    return chatClient.prompt().user(topic).call().content();
    }

    @GetMapping("/dad-jokes-1")
    public String jokes1(@RequestParam(value = "topic", defaultValue = "Dogs") String topic) {
//        PromptTemplate promptTemplate = new PromptTemplate("Tell me a dad joke about {topic}");
//        Prompt prompt = promptTemplate.create(Map.of("topic", topic));
//        return chatClient.call(prompt).getResult().getOutput().getContent();
        return chatClient.prompt()
                .user(userSpec -> userSpec.text("Tell me a dad joke about {topic}").param("topic",topic))
                .user(topic).call().content();
    }

}
