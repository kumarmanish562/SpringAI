package com.spring.openai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
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

    /*
     * ============================================================
     * DEFAULT / OPENAI CHAT CLIENT
     * ============================================================
     *
     * This @Bean method creates a ChatClient and registers it
     * inside the Spring IoC container.
     *
     * Because the method is named:
     *
     *      chatClient
     *
     * the default Bean name will be:
     *
     *      "chatClient"
     *
     * This Bean can then be injected into another class:
     *
     *      public ChatServiceImpl(ChatClient chatClient) {
     *          this.chatClient = chatClient;
     *      }
     *
     *
     * IMPORTANT:
     *
     * ChatClient.Builder is injected by Spring.
     *
     * The builder is then configured with OpenAI-specific
     * default options.
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder){

        /*
         * builder.defaultOptions(...)
         *
         * Configures the default options for this ChatClient.
         *
         * We are using OpenAiChatOptions because this ChatClient
         * is intended to use OpenAI.
         *
         *
         * The options are:
         *
         * model("gpt-4o-mini")
         *      ↓
         * Selects the OpenAI model.
         *
         * temperature(0.3)
         *      ↓
         * Controls the randomness/variation of the response.
         *
         * maxTokens(100)
         *      ↓
         * Limits the amount of generated output.
         */
        return  builder
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4o-mini")
                        .temperature(0.3)
                        .maxTokens(100))

                /*
                 * build()
                 *
                 * Creates the actual ChatClient object.
                 *
                 * Before build():
                 *
                 *      ChatClient.Builder
                 *
                 * After build():
                 *
                 *      ChatClient
                 *
                 *
                 * Complete flow:
                 *
                 * ChatClient.Builder
                 *        ↓
                 * defaultOptions(...)
                 *        ↓
                 * OpenAiChatOptions
                 *        ↓
                 * build()
                 *        ↓
                 * ChatClient
                 */
                .build();
    }

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