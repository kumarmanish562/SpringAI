package com.spring.openai.controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ChatController {

    private final ChatClient chatClient;

    //    public ChatController(ChatClient.Builder builder) {
//        this.chatClient=builder.build();
//    }

    public ChatController(ChatModel chatModel) {

        System.out.println(chatModel.getClass().getName());

        this.chatClient = ChatClient
                .builder(chatModel)
                .build();
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(
            @RequestParam(value = "q", required = true) String q
    ) {

        String resultResponse = chatClient
                .prompt()
                .user(q)
                .call()
                .content();

        return ResponseEntity.ok(resultResponse);
    }
}

