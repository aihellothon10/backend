package com.example.ragtest.adoption;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class Assistant {

//    @Bean
//    ApplicationRunner runner(ChatClient chatClient, VectorStore vectorStore) {
//        return args -> {
//            var req1 = "introduce some dogs that you have";
//            var req2 = "do you have any neurotic dogs?";
//            var req3 = "do you have any dog named Prancer?";
//
//            var content = chatClient
//                    .prompt()
//                    .advisors(new QuestionAnswerAdvisor(vectorStore))
//                            .user(req1)
//                                    .call()
//                                            .content();
//            System.out.println("content [" + content + "]");
//        };
//    }

    // RETRIEVAL AUGMENTED GENERATION(RAG)

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder,
                                 DogRepository repository,
                                 VectorStore vectorStore) {

        repository.findAll().forEach(dog -> {
            var document = new Document("id : %s, name : %s, description : %s"
                    .formatted(dog.id(), dog.name(), dog.description()));
            vectorStore.add(List.of(document));
        });


        var system = """
                You are an AI powered assistant to help people adopt a dog from the adoption agency named Spring's Pet Emporium with locations in Seoul, Las Vegas, Tokyo, Krakow, Singapore, Paris, London, and San Francisco.
                
                """;
        return builder
                .defaultSystem(system)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()),
                        new PromptChatMemoryAdvisor(new InMemoryChatMemory()))
                .build();
    }
}
