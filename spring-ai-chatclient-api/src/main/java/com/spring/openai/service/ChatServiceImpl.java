package com.spring.openai.service;

import com.spring.openai.entity.Tut;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;


/*
 * @Service tells Spring that this class is a Service-layer component.
 *
 * Spring will create an object of ChatServiceImpl and manage it
 * inside the Spring IoC container.
 *
 *
 * Architecture:
 *
 * Controller
 *      ↓
 * ChatService
 *      ↓
 * ChatServiceImpl
 *      ↓
 * ChatClient
 *      ↓
 * ChatModel
 *      ↓
 * OpenAI / Ollama
 */
@Service
public class ChatServiceImpl implements ChatService{


    /*
     * ChatClient is Spring AI's high-level API.
     *
     * It is responsible for building and sending requests
     * to the configured AI model.
     *
     * In this class, ChatClient is used to:
     *
     *      prompt(...)
     *          ↓
     *      call()
     *          ↓
     *      entity(...)
     *
     * The result is finally converted into Java objects.
     */
    private ChatClient chatClient;


    /*
     * Constructor Injection
     *
     * Spring AI provides ChatClient.Builder.
     *
     * builder.build()
     *      ↓
     * creates a ChatClient.
     *
     *
     * The flow is:
     *
     * ChatClient.Builder
     *        ↓
     * ChatClient
     *        ↓
     * ChatModel
     *        ↓
     * AI Provider
     *
     *
     * IMPORTANT:
     *
     * This uses the default ChatClient.Builder configuration.
     *
     * In your earlier multi-provider configuration, you created:
     *
     *      openAiChatClient
     *      ollamaChatClient
     *
     * explicitly.
     *
     * If you want this service to specifically use OpenAI or
     * specifically use Ollama, you should inject the corresponding
     * ChatClient bean with @Qualifier instead of relying on the
     * generic ChatClient.Builder.
     */
    public ChatServiceImpl(ChatClient.Builder builder){
        this.chatClient=builder.build();
    }


//    @Override
//    public String chat(String query) {
//        String prompt="Tell me about virat kholi ?";

//    @Override
//    public Tut chat(String query) {

    /*
     * CURRENT METHOD
     *
     * The interface method is:
     *
     *      List<Tut> chat(String query)
     *
     * So the Service accepts:
     *
     *      String query
     *
     * and returns:
     *
     *      List<Tut>
     *
     *
     * This means the AI response is expected to represent
     * multiple Tutorial objects.
     */
    @Override
    public List<Tut> chat(String query) {


//
//        //call the llm for response
//       String content = chatClient
//                .prompt()
//                .user(prompt)
//                .system("As as expert in cricket.")
//                .call()
//                .content();

        /*
         * This commented code demonstrates the simplest
         * Spring AI text-generation approach.
         *
         *
         * .prompt()
         *      ↓
         * Start building the AI request.
         *
         *
         * .user(prompt)
         *      ↓
         * Adds the user's question/instruction.
         *
         *
         * .system(...)
         *      ↓
         * Gives the AI a system-level instruction.
         *
         *
         * .call()
         *      ↓
         * Sends the request to the AI model.
         *
         *
         * .content()
         *      ↓
         * Extracts the generated text.
         *
         *
         * The result would be a String.
         */


//        Prompt prompt1 = new Prompt(prompt);


        /*
         * Prompt is a Spring AI object representing an AI prompt.
         *
         * Instead of directly writing:
         *
         *      chatClient.prompt().user(query)
         *
         * you can explicitly create a Prompt object.
         *
         * Here:
         *
         *      query
         *          ↓
         *      Prompt
         *
         * Then the Prompt is supplied to ChatClient.
         */
        Prompt prompt1 = new Prompt(query);


//         var content =  chatClient
//                .prompt(prompt1)
//                .call()
//                .content();

        /*
         * This commented version demonstrates how to use the
         * Prompt object with ChatClient.
         *
         *
         *      chatClient.prompt(prompt1)
         *              ↓
         *          call()
         *              ↓
         *          content()
         *
         *
         * The final result is a String containing the AI response.
         *
         * Example:
         *
         *      "Spring Boot is a Java framework..."
         *
         * This is useful when you only need normal text.
         */


//        var metadata =  chatClient
//                .prompt(prompt1)
//                .call()
//                .chatResponse()
//                .getMetadata();
//        System.out.println(metadata);

        /*
         * This commented code demonstrates how to access
         * metadata from the AI response.
         *
         * Instead of:
         *
         *      .content()
         *
         * we use:
         *
         *      .chatResponse()
         *
         * to obtain the complete ChatResponse.
         *
         *
         * ChatResponse can contain information such as:
         *
         *      - model information
         *      - token usage
         *      - generation metadata
         *      - response metadata
         *
         * This becomes useful for monitoring, debugging,
         * cost tracking, and observability.
         */

//
//        return "";
//    }

//        var content =  chatClient
//                .prompt(prompt1)
//                .call()
//                .chatResponse()
//                .getResult()
//                .getOutput()
//                .getText();
//
//        System.out.println(content);
//
//        return content;
//    }

        /*
         * This commented section shows a more detailed way
         * to navigate through the ChatResponse.
         *
         *
         *      chatResponse()
         *          ↓
         *      getResult()
         *          ↓
         *      getOutput()
         *          ↓
         *      getText()
         *
         *
         * Eventually, the generated text is extracted.
         *
         * This is a lower-level approach compared with:
         *
         *      .content()
         *
         * which is much simpler when you only need text.
         */


//        Tut tutorial =  chatClient
//                .prompt(prompt1)
//                .call()
//                .entity(Tut.class);
//
//        return tutorial;
//
//    }

        /*
         * This commented version demonstrates STRUCTURED OUTPUT.
         *
         * Instead of asking the AI for a String, we tell Spring AI:
         *
         *      "Convert the AI response into a Tut object."
         *
         *
         *      .entity(Tut.class)
         *
         * means:
         *
         * AI Response
         *      ↓
         * Spring AI conversion
         *      ↓
         * Tut Java object
         *
         *
         * This version returns ONE Tut object.
         */


        /*
         * CURRENT IMPLEMENTATION
         *
         * We want MULTIPLE Tut objects.
         *
         * Therefore:
         *
         *      List<Tut>
         *
         * is required instead of:
         *
         *      Tut
         *
         *
         * The AI response is converted into:
         *
         *      List<Tut>
         *
         * using ParameterizedTypeReference.
         */
        List<Tut> tutorial =  chatClient
                .prompt(prompt1)
                .call()
                .entity(new ParameterizedTypeReference<List<Tut>>() {

                });


        /*
         * Return the structured result to the Controller.
         *
         * Controller receives:
         *
         *      List<Tut>
         *
         * and then returns it through:
         *
         *      ResponseEntity<List<Tut>>
         *
         * Spring/Jackson converts the Java objects into JSON.
         */
        return tutorial;

    }
}