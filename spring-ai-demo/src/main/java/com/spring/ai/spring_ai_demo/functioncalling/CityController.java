package com.spring.ai.spring_ai_demo.functioncalling;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CityController {

private final ChatClient chatClient;


    public CityController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/function-calling")
    public String functionCallingDemo(@RequestParam(value = "message")String message){
        SystemMessage systemMessage = new SystemMessage("You are a helpful AI Assistant answering questions about cities around the world.");
        UserMessage userMessage = new UserMessage(message);
        OpenAiChatOptions currentWeatherFunction = OpenAiChatOptions.builder().withFunction("currentWeatherFunction").build();
        return chatClient.call(new Prompt(List.of(systemMessage,userMessage),currentWeatherFunction)).getResult().getOutput().getContent();
    }
}
