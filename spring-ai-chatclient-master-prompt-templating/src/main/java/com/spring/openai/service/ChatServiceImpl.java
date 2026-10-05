package com.spring.openai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;

    public ChatServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public String chat(String query) {
        return chatClient
                .prompt()
                .user(query)
                .call()
                .content();
    }

    public String chatTemplate() {

        PromptTemplate strTemplate = PromptTemplate.builder()
                .template("What is {techName}? Tell me an example of {exampleName}")
                .build();

        String renderedMessage = strTemplate
                .render(Map.of(
                        "techName", "Spring Boot",
                        "exampleName", "Dependency Injection"
                ));

        Prompt prompt = new Prompt(renderedMessage);

        var content = this.chatClient.prompt(prompt).call().content();

        return content;
    }
}