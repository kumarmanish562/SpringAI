package com.spring.openai.controllers;

// ChatClient is the high-level API provided by Spring AI.
// We use ChatClient to create prompts and communicate with an AI model.
import com.spring.openai.entity.Tut;
import com.spring.openai.service.ChatService;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


// @RestController tells Spring that this class handles HTTP REST requests.
@RestController

// All endpoints inside this controller start with "/".
@RequestMapping("/")
public class ChatController {


    /*
     * ChatClient is Spring AI's high-level API.
     *
     * Earlier, we were directly using ChatClient inside
     * the controller:
     *
     *      chatClient.prompt()
     *              .user(q)
     *              .call()
     *              .content();
     *
     * That approach works, but it puts AI-related business logic
     * inside the Controller.
     *
     * In a production-style application, we normally separate
     * responsibilities:
     *
     * Controller
     *      ↓
     * Service
     *      ↓
     * ChatClient
     *      ↓
     * ChatModel
     *      ↓
     * LLM
     *
     * Therefore, this ChatClient field is kept here because it
     * was part of the earlier implementation, but the current
     * implementation uses ChatService instead.
     */
    private  ChatClient chatClient;


    /*
     * ChatService contains the actual AI/business logic.
     *
     * Instead of making the Controller responsible for:
     *
     *      prompt()
     *      user()
     *      call()
     *      content()
     *
     * we delegate that work to ChatService.
     *
     * This follows the separation of concerns principle.
     *
     * Controller:
     *      Handles HTTP request/response.
     *
     * Service:
     *      Handles application/business/AI logic.
     */
    private ChatService chatService;


//    public ChatController(ChatClient.Builder chatClientBuilder) {
//        this.chatClient = chatClientBuilder.build();
//    }

    /*
     * This was the earlier constructor-based approach.
     *
     * ChatClient.Builder is provided by Spring AI.
     *
     * Calling:
     *
     *      chatClientBuilder.build()
     *
     * creates a ChatClient.
     *
     * After that, the Controller could directly call:
     *
     *      chatClient.prompt()
     *
     *
     * FLOW:
     *
     * Controller
     *      ↓
     * ChatClient
     *      ↓
     * ChatModel
     *      ↓
     * AI Model
     *
     * This code is commented because we have now moved
     * the AI logic into ChatService.
     */


    /*
     * Current constructor.
     *
     * Spring automatically injects ChatService here.
     *
     * Because there is only one constructor, we don't need
     * to write @Autowired explicitly.
     *
     * Spring essentially does:
     *
     *      ChatService
     *          ↓
     *      ChatController
     *
     * The Controller now depends on the Service instead
     * of directly depending on ChatClient.
     */
    public ChatController(ChatService chatService) {

        // Store the injected ChatService in the field.
        this.chatService = chatService;
    }


//    @GetMapping("/chat")
//    public ResponseEntity<String> chat(
//            @RequestParam(value = "q", required = true) String q
//    ) {
//
//        String resultResponse = this.chatClient
//                .prompt()
//                .user(q)
//                .call()
//                .content();
//
//        return ResponseEntity.ok(resultResponse);
//    }

    /*
     * This was the original implementation.
     *
     * The endpoint:
     *
     *      GET /chat?q=Explain Spring Boot
     *
     * receives the user's question in "q".
     *
     * Then the Controller directly uses ChatClient:
     *
     *      chatClient
     *          ↓
     *      prompt()
     *          ↓
     *      user(q)
     *          ↓
     *      call()
     *          ↓
     *      content()
     *
     * The final String response is returned to the client.
     *
     * PROBLEM WITH THIS APPROACH:
     *
     * The Controller is doing two jobs:
     *
     * 1. Handling HTTP requests.
     * 2. Executing AI/business logic.
     *
     * Therefore, we later moved the AI logic into ChatService.
     */


//    @GetMapping("/chat")
//    public ResponseEntity<String> chat(
//            @RequestParam(value = "q", required = true) String q
//    ) {
//
//
//        return ResponseEntity.ok(chatService.chat(q));
//    }

    /*
     * This is the next version of the Controller.
     *
     * Notice that ChatClient is no longer used directly.
     *
     * Instead:
     *
     *      chatService.chat(q)
     *
     * is called.
     *
     * The Controller only handles:
     *
     *      HTTP request
     *             ↓
     *      extract q
     *             ↓
     *      call Service
     *             ↓
     *      return HTTP response
     *
     *
     * The Service handles:
     *
     *      ChatClient
     *          ↓
     *      prompt()
     *          ↓
     *      AI model
     *
     * This is a cleaner architecture.
     *
     * However, this version returns:
     *
     *      ResponseEntity<String>
     *
     * which means the Service returns a String.
     */


    /*
     * CURRENT IMPLEMENTATION
     *
     * The current endpoint returns:
     *
     *      ResponseEntity<List<Tut>>
     *
     * instead of:
     *
     *      ResponseEntity<String>
     *
     * This means the AI/service layer is now producing
     * structured Java objects represented by the Tut entity.
     *
     *
     * Example conceptual response:
     *
     * [
     *     {
     *         "title": "Spring Boot",
     *         ...
     *     },
     *     {
     *         "title": "Spring AI",
     *         ...
     *     }
     * ]
     *
     * Spring automatically converts the Java List<Tut>
     * into JSON using Jackson.
     */

    @GetMapping("/chat")
    public ResponseEntity<List<Tut>> chat(
            @RequestParam(value = "q", required = true) String q
    ) {

        /*
         * q contains the question/request sent by the client.
         *
         * Example:
         *
         *      GET /chat?q=Give me Spring Boot tutorials
         *
         * q:
         *      "Give me Spring Boot tutorials"
         *
         * We pass that question to ChatService.
         *
         * The Controller does NOT need to know how the AI
         * generates the result.
         */

        /*
         * chatService.chat(q)
         *
         * calls the Service layer.
         *
         * The Service is responsible for:
         *
         *      Prompt creation
         *      ↓
         *      ChatClient
         *      ↓
         *      ChatModel
         *      ↓
         *      LLM
         *      ↓
         *      AI response
         *      ↓
         *      Convert response into List<Tut>
         */

        return ResponseEntity.ok(chatService.chat(q));
    }


}