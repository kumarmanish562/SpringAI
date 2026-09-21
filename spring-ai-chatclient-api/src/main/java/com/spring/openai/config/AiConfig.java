package com.spring.openai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// @Configuration tells Spring that this class contains
// Spring Bean definitions.
//
// Spring will scan this class and execute the methods
// annotated with @Bean.
//
// The objects returned by those methods will be managed
// by the Spring IoC container.
@Configuration
public class AiConfig {


    // ============================================================
    // OPENAI CHAT CLIENT
    // ============================================================

    /*
     * @Bean tells Spring:
     *
     * "Create and manage the object returned by this method."
     *
     * name = "openAiChatClient"
     *
     * gives this Bean a specific name.
     *
     * This name is important because later we use:
     *
     * @Qualifier("openAiChatClient")
     *
     * to select this particular ChatClient.
     *
     *
     * Method parameter:
     *
     * OpenAiChatModel chatModel
     *
     * Spring AI automatically creates/configures the
     * OpenAiChatModel using your OpenAI configuration.
     *
     *
     * Then:
     *
     * ChatClient.builder(chatModel).build()
     *
     * creates a ChatClient that internally uses OpenAI.
     */

//    @Bean(name = "openAiChatClient")
//    public ChatClient openAiChatModel(OpenAiChatModel chatModel) {
//
//        return ChatClient
//                .builder(chatModel)
//                .build();
//
//    }


    // ============================================================
    // OLLAMA CHAT CLIENT
    // ============================================================

    /*
     * This creates another ChatClient.
     *
     * Bean name:
     *
     *      "ollamaChatClient"
     *
     * Therefore, we can later select it using:
     *
     *      @Qualifier("ollamaChatClient")
     *
     *
     * The parameter:
     *
     *      OllamaChatModel chatModel
     *
     * is the ChatModel implementation responsible for
     * communicating with Ollama.
     *
     *
     * Then:
     *
     *      ChatClient.builder(chatModel).build()
     *
     * creates a ChatClient backed by Ollama.
     */

//    @Bean(name = "ollamaChatClient")
//    public ChatClient ollamChatModel(OllamaChatModel chatModel) {
//
//        return ChatClient
//                .builder(chatModel)
//                .build();
//
//    }

}