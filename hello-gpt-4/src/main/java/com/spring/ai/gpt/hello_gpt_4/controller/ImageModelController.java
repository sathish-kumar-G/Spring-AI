package com.spring.ai.gpt.hello_gpt_4.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ImageModelController {

    private final ChatClient chatClient;

    public ImageModelController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.defaultSystem("you are loud assistant for responsible for all capital letters").build();
    }

    @GetMapping("/image-describe")
    public String describeImage() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("/images/images.jpeg");
        return chatClient.prompt().user(userSpec -> userSpec.media(MimeTypeUtils.IMAGE_JPEG, classPathResource).text("Can you please explain what you see in the following image?")).call().content();
    }
}
