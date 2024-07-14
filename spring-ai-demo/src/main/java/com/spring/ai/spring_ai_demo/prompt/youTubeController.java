package com.spring.ai.spring_ai_demo.prompt;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class youTubeController {

    private final ChatClient chatClient;

    @Value("classpath:/prompts/youtube.st")
    private Resource resource;

    public youTubeController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/popular")
    public String findPopularYouTubersStepOne(@RequestParam(name = "genre", defaultValue = "sports") String genre) {
        String message = """
                List 10 of the most popular YouTubers in {genre} along with their current subscriber counts. If you don't know
                the answer , just say "I don't know".
                """;
        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create(Map.of("genre", genre));
        return chatClient.call(prompt).getResult().getOutput().getContent();
    }

    @GetMapping("/popular-1")
    public String findPopularYouTubersStepTwo(@RequestParam(name = "genre", defaultValue = "sports") String genre) {

        PromptTemplate promptTemplate = new PromptTemplate(resource);
        Prompt prompt = promptTemplate.create(Map.of("genre", genre));
        return chatClient.call(prompt).getResult().getOutput().getContent();
    }

}
