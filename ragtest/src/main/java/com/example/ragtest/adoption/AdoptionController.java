package com.example.ragtest.adoption;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@ResponseBody
@RequestMapping("/adoptions")
class AdoptionController {

    private final DogRepository repository;

    private final ApplicationEventPublisher publisher;

    private final VectorStore vectorStore;

    private final ChatClient chatClient;

    AdoptionController(DogRepository repository, ApplicationEventPublisher publisher, VectorStore vectorStore, ChatClient chatClient) {
        this.repository = repository;
        this.publisher = publisher;
        this.vectorStore = vectorStore;
        this.chatClient = chatClient;
    }

    @GetMapping("/test")
    void test() {

        System.out.println("test" + vectorStore.getName());
        var ret = vectorStore.similaritySearch("name with Prancer");
        System.out.println("ret [" + ret + "]");


    }

    @GetMapping("/chat/{num}")
    void chat(@PathVariable Integer num) {
        String request;
        switch (num) {
            case 1:
                request = "introduce some dogs that you have";
                break;
            case 2:
                request = "do you have any neurotic dogs?";
                break;
            case 3:
                request = "do you have any dog named Prancer?";
                break;
            default:
                request = "Please provide a valid request number.";
        }

        var ret = vectorStore.similaritySearch("name with Prancer");
        System.out.println("vector similarity Search... [" + ret + "]");

        var content = chatClient
                .prompt()
                .user(request)
                .call()
                .content();

        System.out.println("content [" + content + "]");
    }

    @GetMapping("/name/{name}")
    void chatName(@PathVariable String name) {
        String request = "do you have any dog name with %s?".formatted(name);

        var ret = vectorStore.similaritySearch("name with %s".formatted(name));
        System.out.println("vector similarity Search... [" + ret + "]");

        var content = chatClient
                .prompt()
                .user(request)
                .call()
                .content();

        System.out.println("content [" + content + "]");
    }

    @GetMapping("/q")
    void chatQuery(@RequestParam String query) {
        var content = chatClient
                .prompt()
                .user(query)
                .call()
                .content();

        System.out.println("content [" + content + "]");
    }


    @GetMapping("/{id}")
    Dog list(@PathVariable Integer id) {
        return this.repository.findById(id).orElse(null);
    }

    @PostMapping("/{id}")
    void adopt(@PathVariable Integer id, @RequestBody Map<String, String> owner) {
        this.repository.findById(id).ifPresent(dog -> {
            var saved = this.repository.save(new Dog(id, dog.name(), dog.description(), dog.dob(), owner.get("name")));
//            this.publisher.publishEvent(new DogAdoptedEvent(dog.id(), dog.name()));
            System.out.println("saved [" + saved + "]");
        });

    }

}
