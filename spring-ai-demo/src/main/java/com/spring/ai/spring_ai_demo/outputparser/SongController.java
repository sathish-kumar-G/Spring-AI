package com.spring.ai.spring_ai_demo.outputparser;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.ai.parser.ListOutputParser;
import org.springframework.ai.parser.MapOutputParser;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SongController {

    private final ChatClient chatClient;

    public SongController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/songs-list")
    public List<String> getSongs(@RequestParam(name = "artist", defaultValue = "Aniruth") String artist) {
        String message = """
                Please give me a list of top 10 songs for the artist {artist}. If you don't know the answer, Just say :"I don't know".
                {format}
                """;

        ListOutputParser outputParser = new ListOutputParser(new DefaultConversionService());

        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create(Map.of("artist", artist, "format", outputParser.getFormat()));
        ChatResponse chatResponse = chatClient.call(prompt);
        return outputParser.parse(chatResponse.getResult().getOutput().getContent());
    }

    @GetMapping("/songs-map")
    public Map<String, Object> getSongsMap(@RequestParam(name = "artist", defaultValue = "Illayaraja") String artist) {
        String message = """
                Please give me a list of top 10 songs for the artist {artist}. If you don't know the answer, Just say :"I don't know".
                {format}
                """;

        MapOutputParser outputParser = new MapOutputParser();

        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create(Map.of("artist", artist, "format", outputParser.getFormat()));
        ChatResponse chatResponse = chatClient.call(prompt);
        return outputParser.parse(chatResponse.getResult().getOutput().getContent());
    }

    @GetMapping("/songs-bean")
    public Artist getSongsBean(@RequestParam(name = "artist", defaultValue = "Illayaraja") String artist) {
        String message = """
                Please give me a list of top 10 songs for the artist {artist}. If you don't know the answer, Just say :"I don't know".
                {format}
                """;

        var outputParser = new BeanOutputParser<>(Artist.class);

        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create(Map.of("artist", artist, "format", outputParser.getFormat()));
        ChatResponse chatResponse = chatClient.call(prompt);
        return outputParser.parse(chatResponse.getResult().getOutput().getContent());
    }
}

