package com.spring.ai.gpt.hello_gpt_4.actor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
public class ActorController {

    private final ChatClient chatClient;

    public ActorController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/films-string")
    public String getActorFilmsString() {
        return chatClient.prompt()
                .user("which film is highest collection in tamil cinema, give me the top 10 till 2024" +
                        ".")
                .call()
                .content();
    }

    @GetMapping("/films")
    public ActorFilms getActorFilms() {
        return chatClient.prompt()
                .user("Generate a filmography for a Vijay.")
                .call()
                .entity(ActorFilms.class);
    }

    @GetMapping("/films-list")
    public List<ActorFilms> listActorFilms() {
        return chatClient.prompt()
                .user("Generate a filmography for the actors Vijay, Ajith and Kamal")
                .call()
                .entity(new ParameterizedTypeReference<>() {});
    }

    @GetMapping("/films-by-actor")
    public ActorFilms getActorFilmsByName(@RequestParam(defaultValue = "Rajinikanth") String actor) {
        return chatClient.prompt()
                .user(u -> u.text("Generate a filmography for the actor {actor}").param("actor",actor))
                .call()
                .entity(ActorFilms.class);
    }

    @GetMapping("/stream")
    public Flux<String> getActorFilmsByNameStream(@RequestParam(defaultValue = "I'm visiting San Francisco next month, what are 10 places I must visit?") String message) {
        return chatClient.prompt()
                .user(message)
                .stream().content();
    }

}
