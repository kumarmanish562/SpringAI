package com.spring.openai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;


// @Service tells Spring that this class belongs to the Service layer.
//
// Spring automatically creates and manages an object of this class
// inside the Spring IoC container.
//
// Architecture:
//
// ChatController
//       ↓
// ChatService
//       ↓
// ChatServiceImpl
//       ↓
// ChatClient
//       ↓
// OpenAI Chat Model
//       ↓
// OpenAI API
@Service
public class ChatServiceImpl implements ChatService {

    /*
     * ChatClient is Spring AI's high-level API for communicating
     * with an AI model.
     *
     * Instead of directly working with the low-level ChatModel,
     * we normally use ChatClient to create and send prompts.
     *
     * Example:
     *
     * chatClient
     *      .prompt()
     *      .user("Explain Spring Boot")
     *      .call()
     *      .content();
     *
     * The final result is a String containing the AI response.
     *
     * final means that once the ChatClient is assigned through
     * the constructor, the reference cannot be reassigned.
     */
    private final ChatClient chatClient;


    /*
     * Constructor Injection
     *
     * Spring automatically injects a ChatClient bean into this
     * constructor.
     *
     * The injected ChatClient is then stored in the field:
     *
     *      this.chatClient = chatClient;
     *
     *
     * Why constructor injection?
     *
     * It makes the dependency explicit.
     *
     * ChatServiceImpl cannot work without ChatClient, so
     * ChatClient is provided when ChatServiceImpl is created.
     *
     *
     * Flow:
     *
     * Spring IoC Container
     *        ↓
     * creates ChatClient
     *        ↓
     * injects ChatClient here
     *        ↓
     * creates ChatServiceImpl
     */
    public ChatServiceImpl(ChatClient chatClient) {

        // Store the ChatClient provided by Spring
        // in the class-level chatClient field.
        this.chatClient = chatClient;


//                .defaultOptions(OpenAiChatOptions.builder()
//                        .model("gpt-4o-mini")
//                        .temperature(0.3)
//                        .maxTokens(100))
//                .build();

        /*
         * IMPORTANT:
         *
         * The above code is commented because it belongs to the
         * ChatClient.Builder configuration approach.
         *
         * defaultOptions(...) is a method used while BUILDING
         * a ChatClient.
         *
         * It is not something we normally call directly on an
         * already-created ChatClient.
         *
         *
         * The intended Builder flow is:
         *
         * ChatClient.Builder
         *        ↓
         * defaultOptions(...)
         *        ↓
         * OpenAiChatOptions.builder()
         *        ↓
         * model(...)
         * temperature(...)
         * maxTokens(...)
         *        ↓
         * build()
         *        ↓
         * ChatClient
         *
         *
         * In the current constructor, however, we receive an
         * already-created ChatClient:
         *
         *      ChatServiceImpl(ChatClient chatClient)
         *
         * Therefore, we simply assign it:
         *
         *      this.chatClient = chatClient;
         */
    }

    /*
     * @Override tells Java that this method implements
     * the chat() method declared inside ChatService.
     *
     * The interface contains:
     *
     *      String chat(String query);
     *
     * Therefore, the implementation must also return String
     * and accept String query.
     */
    @Override
    public String chat(String query) {


//        Prompt prompt = new Prompt(query, OpenAiChatOptions.builder()
//                .model("gpt-4o-mini")
//                .temperature(0.3)
//                .maxTokens(100)
//                .build());

        /*
         * This commented code demonstrates another way to create
         * a Prompt with model-specific options.
         *
         *
         * Prompt contains the information that will be sent
         * to the AI model.
         *
         *
         * The first argument:
         *
         *      query
         *
         * represents the user's actual question.
         *
         *
         * The second argument:
         *
         *      OpenAiChatOptions.builder()
         *
         * allows OpenAI-specific options to be associated
         * with this particular prompt.
         *
         *
         * Example options:
         *
         *      model("gpt-4o-mini")
         *          ↓
         *      Selects the OpenAI model.
         *
         *      temperature(0.3)
         *          ↓
         *      Controls response randomness.
         *
         *      maxTokens(100)
         *          ↓
         *      Limits generated output.
         *
         *
         * The complete conceptual flow is:
         *
         * User query
         *      ↓
         * Prompt
         *      ↓
         * OpenAiChatOptions
         *      ↓
         * ChatClient
         *      ↓
         * OpenAI
         */


        /*
         * Creates a simple Spring AI Prompt object
         * using the user's query.
         *
         * For example, if the user sends:
         *
         *      "Explain Spring Boot"
         *
         * then:
         *
         *      query = "Explain Spring Boot"
         *
         * and:
         *
         *      Prompt prompt = new Prompt(query);
         *
         * creates a Prompt containing that user request.
         *
         *
         * IMPORTANT:
         *
         * In the current code, this Prompt object is created
         * but is not actually used below.
         *
         * The actual ChatClient call uses:
         *
         *      .prompt()
         *      .user(query)
         *
         * instead.
         *
         * So this Prompt variable is currently only demonstrating
         * how a Prompt can be created explicitly.
         */
        Prompt prompt = new Prompt(query);

        /*
         * This variable stores the final AI-generated response.
         *
         * The name "tutorial" is only a variable name.
         *
         * Its actual type is inferred by var.
         *
         * Because .content() returns the generated text,
         * the actual type of tutorial here is:
         *
         *      String
         *
         *
         * Complete chain:
         *
         * chatClient
         *      ↓
         * .prompt()
         *      ↓
         * .user(query)
         *      ↓
         * .call()
         *      ↓
         * .content()
         *      ↓
         * String
         */
        var tutorial = chatClient
                /*
                 * .prompt()
                 *
                 * Starts building a new AI request.
                 *
                 * It creates the request specification that
                 * will eventually be sent to the configured
                 * ChatModel.
                 */
                .prompt()

                /*
                 * .user(query)
                 *
                 * Adds the user's message to the prompt.
                 *
                 * Example:
                 *
                 * query =
                 *      "Explain Spring AI"
                 *
                 * The user message becomes:
                 *
                 *      User:
                 *      Explain Spring AI
                 */
                .user(query)

                /*
                 * .call()
                 *
                 * Actually executes the request.
                 *
                 * Before call(), we are only building the request.
                 *
                 * call() sends the request through the configured
                 * ChatModel to the AI provider.
                 *
                 *
                 * Flow:
                 *
                 * ChatClient
                 *      ↓
                 * Prompt
                 *      ↓
                 * ChatModel
                 *      ↓
                 * OpenAI
                 *      ↓
                 * AI response
                 */
                .call()

                /*
                 * .content()
                 *
                 * Extracts only the generated text from the
                 * AI response.
                 *
                 * For example, the complete AI response may
                 * contain metadata, usage information, model
                 * information, etc.
                 *
                 * .content() gives us the actual text:
                 *
                 *      "Spring AI is a framework..."
                 *
                 * Therefore the result is a String.
                 */
                .content();

        /*
         * Return the AI-generated String to the Controller.
         *
         * The flow is:
         *
         * AI response
         *      ↓
         * String tutorial
         *      ↓
         * return tutorial
         *      ↓
         * ChatController
         *      ↓
         * ResponseEntity<String>
         *      ↓
         * HTTP response
         */
        return tutorial;
    }
}