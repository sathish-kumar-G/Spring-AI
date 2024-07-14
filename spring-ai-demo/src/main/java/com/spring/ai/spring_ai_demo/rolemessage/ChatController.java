package com.spring.ai.spring_ai_demo.rolemessage;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/dad-joke")
    public String demo(@RequestParam(name = "message",defaultValue = "Tell me a mom joke") String message){
        return chatClient.call(message);
    }

    @GetMapping("/prompt")
    public String promptDemo(){
        return chatClient.call(new Prompt("Tell me a dad joke")).getResult().getOutput().getContent();
    }


    @GetMapping("/dad-jokes")
            public String roleMessageDemo(){
        var systemMessage = new SystemMessage("Your primary function is o tell dad jokes, if someone asks you for any other type of joke please tell them you only know dad jokes");
        var userMessage = new UserMessage("Tell me a serious joke about universe");

        Prompt prompt = new Prompt(List.of(systemMessage,userMessage));

        return chatClient.call(prompt).getResult().getOutput().getContent();
    }

}
