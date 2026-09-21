package com.spring.openai.controllers;

// ChatClient is the high-level API provided by Spring AI.
// We use ChatClient to create prompts and communicate with an AI model.
import org.springframework.ai.chat.client.ChatClient;

// ChatModel is the lower-level abstraction representing an AI chat model.
// It can represent models from providers such as OpenAI, Ollama, etc.
import org.springframework.ai.chat.model.ChatModel;

// Concrete ChatModel implementation for Ollama.
import org.springframework.ai.ollama.OllamaChatModel;

// Concrete ChatModel implementation for OpenAI.
import org.springframework.ai.openai.OpenAiChatModel;

// @Qualifier is used when multiple beans of the same type exist.
// Here we have multiple ChatClient beans, so @Qualifier tells Spring
// exactly which ChatClient should be injected.
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


// @RestController tells Spring that this class handles HTTP REST requests.
@RestController

// All endpoints inside this controller start with "/".
@RequestMapping("/")
public class ChatController {


    // ============================================================
    // OLD / SINGLE CHAT CLIENT APPROACH
    // ============================================================

    // In the beginning, if our application had only one AI provider,
    // we could simply have one ChatClient.

    // private final ChatClient chatClient;


    // ============================================================
    // CHATCLIENT BUILDER APPROACH
    // ============================================================

    /*
     * ChatClient.Builder can be automatically provided by Spring AI.
     *
     * builder.build() creates a ChatClient using the ChatModel
     * configured by Spring AI.
     *
     * This approach is convenient when we have one primary model.
     */

    //    public ChatController(ChatClient.Builder builder) {
    //        this.chatClient=builder.build();
    //    }


    // ============================================================
    // MULTIPLE CHAT CLIENTS
    // ============================================================

    /*
     * Now we want to work with MULTIPLE AI providers.
     *
     * We have:
     *
     *      OpenAI
     *         ↓
     *    OpenAiChatModel
     *         ↓
     *    OpenAI ChatClient
     *
     *
     *      Ollama
     *         ↓
     *    OllamaChatModel
     *         ↓
     *    Ollama ChatClient
     *
     *
     * Therefore, we maintain two different ChatClient objects.
     */

    private ChatClient openAiChatClient;

    private ChatClient ollamaChatClient;


    // ============================================================
    // DIRECT ChatModel APPROACH
    // ============================================================

    /*
     * Earlier, we could inject a generic ChatModel.
     *
     * public ChatController(ChatModel chatModel)
     *
     * Spring would provide the ChatModel bean.
     *
     * Then we could create a ChatClient from that ChatModel.
     *
     * However, when our application contains multiple ChatModels,
     * such as OpenAI + Ollama, simply injecting ChatModel can become
     * ambiguous because Spring may have more than one ChatModel bean.
     */

//    public ChatController(ChatModel chatModel) {
//
//        System.out.println(chatModel.getClass().getName());
//
//        this.chatClient = ChatClient
//                .builder(chatModel)
//                .build();
//    }


    // ============================================================
    // OPENAI + OLLAMA ChatModel APPROACH
    // ============================================================

    /*
     * Another approach is to directly inject both concrete models:
     *
     *      OpenAiChatModel
     *      OllamaChatModel
     *
     * Then create a ChatClient for each model.
     *
     * This gives us:
     *
     *      OpenAiChatModel
     *             ↓
     *      ChatClient.builder(...)
     *             ↓
     *      openAiChatClient
     *
     *
     *      OllamaChatModel
     *             ↓
     *      ChatClient.builder(...)
     *             ↓
     *      ollamaChatClient
     *
     * IMPORTANT:
     *
     * In the original commented code below, there is a mistake:
     *
     * this.openAiChatClient = ChatClient.builder(ollamaChatModel).build();
     *
     * It should assign the Ollama client to:
     *
     * this.ollamaChatClient
     *
     * The commented code is kept below exactly as requested.
     */

//    public ChatController(OpenAiChatModel openAiChatModel, OllamaChatModel ollamaChatModel){
//
//        this.openAiChatClient=ChatClient.builder(openAiChatModel).build();
//        this.openAiChatClient= ChatClient.builder(ollamaChatModel).build();
//    }


    // ============================================================
    // CURRENT APPROACH — MULTIPLE ChatClient BEANS
    // ============================================================

    /*
     * Here we are injecting TWO ChatClient beans.
     *
     * Both objects have the same Java type:
     *
     *      ChatClient
     *
     * Therefore, Spring needs to know:
     *
     *      Which ChatClient is for OpenAI?
     *      Which ChatClient is for Ollama?
     *
     * This is why we use @Qualifier.
     *
     * @Qualifier("openAiChatClient")
     *          ↓
     * tells Spring to inject the ChatClient bean named
     * "openAiChatClient".
     *
     *
     * @Qualifier("ollamaChatClient")
     *          ↓
     * tells Spring to inject the ChatClient bean named
     * "ollamaChatClient".
     *
     *
     * So the dependency injection looks like:
     *
     *      Spring Container
     *           │
     *           ├── openAiChatClient
     *           │       ↓
     *           │    OpenAI
     *           │
     *           └── ollamaChatClient
     *                   ↓
     *                 Ollama
     */

    public ChatController(
            @Qualifier("openAiChatClient")
            ChatClient openAiChatClient,

            @Qualifier("ollamaChatClient")
            ChatClient ollamaChatClient
    ) {

        // Store the OpenAI ChatClient in the controller.
        this.openAiChatClient = openAiChatClient;

        // Store the Ollama ChatClient in the controller.
        this.ollamaChatClient = ollamaChatClient;
    }


    // ============================================================
    // SINGLE ChatClient ENDPOINT
    // ============================================================

    /*
     * This was the original endpoint when we had only one ChatClient.
     *
     * The request would be:
     *
     * GET /chat?q=Explain Spring Boot
     *
     * Then:
     *
     * Controller
     *     ↓
     * ChatClient
     *     ↓
     * ChatModel
     *     ↓
     * AI Model
     *
     * This code is kept commented exactly as requested.
     */

//    @GetMapping("/chat")
//    public ResponseEntity<String> chat(
//            @RequestParam(value = "q", required = true) String q
//    ) {
//
//        String resultResponse = chatClient
//                .prompt()
//                .user(q)
//                .call()
//                .content();
//
//        return ResponseEntity.ok(resultResponse);
//    }


    // ============================================================
    // OPENAI CHATCLIENT ENDPOINT
    // ============================================================

    /*
     * This version uses the OpenAI ChatClient.
     *
     *      this.openAiChatClient
     *              ↓
     *         OpenAI ChatModel
     *              ↓
     *            OpenAI
     *              ↓
     *          GPT Model
     *
     * If this code is enabled, the request will be processed
     * using OpenAI.
     *
     * Example:
     *
     * GET /chat?q=Explain Spring Security
     */

//    @GetMapping("/chat")
//    public ResponseEntity<String> chat(
//            @RequestParam(value = "q", required = true) String q
//    ) {
//
//        String resultResponse = this.openAiChatClient
//                .prompt()
//                .user(q)
//                .call()
//                .content();
//
//        return ResponseEntity.ok(resultResponse);
//    }


    // ============================================================
    // CURRENT ENDPOINT — OLLAMA
    // ============================================================

    /*
     * This endpoint currently uses the Ollama ChatClient.
     *
     * Request:
     *
     * GET /chat?q=Explain Spring Boot
     *
     *
     * Step 1:
     *
     * Request reaches the controller.
     *
     *
     * Step 2:
     *
     * q contains:
     *
     * "Explain Spring Boot"
     *
     *
     * Step 3:
     *
     * We select:
     *
     *      ollamaChatClient
     *
     *
     * Step 4:
     *
     * .prompt()
     *
     * starts building the AI request.
     *
     *
     * Step 5:
     *
     * .user(q)
     *
     * adds the user's question to the prompt.
     *
     *
     * Step 6:
     *
     * .call()
     *
     * sends the request to the configured AI model.
     *
     *
     * Step 7:
     *
     * .content()
     *
     * extracts the generated text from the AI response.
     *
     *
     * Step 8:
     *
     * ResponseEntity.ok(...)
     *
     * returns HTTP 200 with the AI response.
     *
     *
     * COMPLETE FLOW:
     *
     * Client
     *   ↓
     * GET /chat?q=Explain Spring Boot
     *   ↓
     * ChatController
     *   ↓
     * ollamaChatClient
     *   ↓
     * OllamaChatModel
     *   ↓
     * Ollama
     *   ↓
     * Llama / configured Ollama model
     *   ↓
     * AI Response
     *   ↓
     * ResponseEntity
     *   ↓
     * Client
     */

    @GetMapping("/chat")
    public ResponseEntity<String> chat(
            @RequestParam(value = "q", required = true) String q
    ) {

        String resultResponse = this.ollamaChatClient
                .prompt()
                .user(q)
                .call()
                .content();

        return ResponseEntity.ok(resultResponse);
    }
}