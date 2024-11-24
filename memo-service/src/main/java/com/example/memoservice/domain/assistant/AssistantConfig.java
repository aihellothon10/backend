package com.example.memoservice.domain.assistant;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AssistantConfig {

    @Bean
    public ChatClient assistChatClient(ChatClient.Builder builder,
                                       VectorStore vectorStore) {

        String systemPrompt = """
                당신은 양육자 또는 아이를 기르는 사람들에게 유익한 정보를 제공해주는 선생님입니다.
                당신은 아이에 대한 지식을 title, content 형태의로 가지고 있습니다.
                당신이 가진 정보 중 질문의 대답에 가장 적절한 정보를 선택해서 설명해주세요.
                설명은 3문장으로 요약해주세요.
                
                답변에 사용할 적절한 정보가 없다면 가장 유사한 정보를 기반으로 답변을 작성해주세요.
                
                유사한 것도 없으면 답변을 얻을 수 있는 질문을 4개 추천해주세요.
                """;

        return builder
                .defaultSystem(systemPrompt)
                .defaultAdvisors(
                        new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults())
                )
                .build();
    }

    @Bean
    public ChatClient promptChatClient(ChatClient.Builder builder) {

        String systemPrompt = """
                당신은 양육자 또는 아이를 기르는 사람들에게 유익한 정보를 제공해주는 선생님입니다.
                당신은 아이에 대한 지식을 title, content 형태의로 가지고 있습니다.
                당신이 가진 정보 중 질문의 대답에 가장 적절한 정보를 선택해서 설명해주세요.
                설명은 3문장으로 요약해주세요.
                
                답변에 사용할 적절한 정보가 없다면 가장 유사한 정보를 기반으로 답변을 작성해주세요.
                
                유사한 것도 없으면 답변을 얻을 수 있는 질문을 4개 추천해주세요.
                """;

        return builder
                .defaultSystem(systemPrompt)
                .build();
    }


}
