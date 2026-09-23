package com.spring.openai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;


// @Service tells Spring that this class belongs to the Service layer.
//
// Spring automatically creates an object of this class
// and manages it inside the Spring IoC container.
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
     * ChatClient is Spring AI's high-level API.
     *
     * It is used to create prompts and communicate
     * with the configured AI model.
     *
     * We declare it as final because the dependency is
     * provided through constructor injection and should not
     * be reassigned later.
     */
    private final ChatClient chatClient;


    /*
     * Constructor Injection
     *
     * Spring automatically provides the ChatClient bean
     * that we configured in AiConfig.
     *
     * The injected ChatClient is stored in the class field.
     *
     * Flow:
     *
     * Spring IoC Container
     *        ↓
     * ChatClient Bean
     *        ↓
     * ChatServiceImpl Constructor
     *        ↓
     * this.chatClient
     */
    public ChatServiceImpl(ChatClient chatClient) {

        // Store the ChatClient provided by Spring.
        this.chatClient = chatClient;
    }


    /*
     * @Override means this method implements the
     * chat() method defined in the ChatService interface.
     *
     * The method accepts:
     *
     *      String query
     *
     * which represents the user's question.
     *
     * It returns:
     *
     *      String
     *
     * which contains the AI-generated response.
     */
    @Override
    public String chat(String query) {

        /*
         * SYSTEM PROMPT
         *
         * A system prompt provides instructions to the AI
         * about how it should behave.
         *
         * It is different from the user's question.
         *
         * System message:
         *      Defines AI behavior and rules.
         *
         * User message:
         *      Contains the actual question/request.
         *
         *
         * We are using a Java text block (""")
         * so that we can write a multi-line String
         * without manually using \n.
         */
        String systemQuery = """
                You are an expert software engineer and coding mentor.

                Your responsibilities:
                1. Always prefer Java when providing programming examples.
                2. Explain concepts in a simple and beginner-friendly way.
                3. Provide clean, readable, and production-quality code.
                4. Explain the code step by step when necessary.
                5. If the question is about Spring Boot, use modern Spring Boot practices.
                6. If the question is about Spring AI, explain the relevant Spring AI concepts clearly.
                7. Mention important mistakes or common pitfalls when relevant.
                8. Do not provide unnecessarily complicated solutions.
                9. If the question is ambiguous, ask for clarification.
                10. Structure technical answers using headings, bullet points, and code blocks when useful.
                """;


        /*
         * Send the request to the AI model.
         *
         * The chain follows this sequence:
         *
         * chatClient
         *      ↓
         * prompt()
         *      ↓
         * system(systemQuery)
         *      ↓
         * user(query)
         *      ↓
         * call()
         *      ↓
         * content()
         *
         *
         * This creates a request containing two messages:
         *
         * SYSTEM:
         *      "You are an expert software engineer..."
         *
         * USER:
         *      The actual question provided by the user.
         */
        var tutorial = chatClient

                /*
                 * prompt()
                 *
                 * Starts creating a new AI request.
                 */
                .prompt()

                /*
                 * system(systemQuery)
                 *
                 * Adds the system-level instructions.
                 *
                 * These instructions tell the AI how it should
                 * behave while answering the user's question.
                 */
                .system(systemQuery)

                /*
                 * user(query)
                 *
                 * Adds the actual user's question.
                 *
                 * Example:
                 *
                 * query =
                 *      "Explain dependency injection in Spring Boot"
                 *
                 * The AI receives this as the USER message.
                 */
                .user(query)

                /*
                 * call()
                 *
                 * Executes the AI request.
                 *
                 * Until call() is reached, we are only building
                 * the request.
                 *
                 * call() sends the request to the configured
                 * ChatModel/OpenAI provider.
                 */
                .call()

                /*
                 * content()
                 *
                 * Extracts the actual text generated by the AI.
                 *
                 * The AI response can contain more information
                 * internally, such as metadata and usage details.
                 *
                 * content() gives us the generated text.
                 *
                 * Therefore, tutorial is a String.
                 */
                .content();


        /*
         * Return the AI-generated response to the caller.
         *
         * The caller is normally ChatController.
         *
         * Flow:
         *
         * AI Response
         *      ↓
         * tutorial
         *      ↓
         * return tutorial
         *      ↓
         * ChatController
         *      ↓
         * ResponseEntity<String>
         *      ↓
         * HTTP Response
         */
        return tutorial;
    }
}