package spring_ollama.spring_ai_ollama_a.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RAGServiceTests {

    @Autowired
    private RAGService ragService;

    // ---------------------------------------------------------
    // Test PDF Ingestion
    // ---------------------------------------------------------

    @Test
    public void testIngest() {

        ragService.ingestPdfToVectorStore();

        System.out.println(
                "PDF successfully ingested into PGVector."
        );
    }


    // ---------------------------------------------------------
    // Test RAG
    // ---------------------------------------------------------

    @Test
    public void testAskAi() {

        // First make sure PDF data exists
        ragService.ingestPdfToVectorStore();

        String answer =
                ragService.askAi(
                        "I am getting a “Video cannot be played” or “Unsupported\n" +
                                "format” error\n"
                );

        System.out.println(
                "========== RAG ANSWER =========="
        );

        System.out.println(answer);

        System.out.println(
                "================================"
        );
    }


    // ---------------------------------------------------------
    // Test RAG with Advisor
    // ---------------------------------------------------------

    @Test
    public void testChatMemory() {

        String userId = "user-2";

        // Store information
//        String answer1 = ragService.askAIWithAdvisors(
//                """
//                My name is Manish.
//                Please remember my name for our future conversations.
//                """,
//                userId
//        );

        String answer1 = ragService.askAIWithAdvisors(
                "What are you view on ModiJi ",
                userId
        );

        System.out.println(
                "========== FIRST RESPONSE =========="
        );

        System.out.println(answer1);


        // Retrieve information

//        String answer2 = ragService.askAIWithAdvisors(
//                """
//                What is my name?
//                """,
//                userId
//        );

//        String answer2 = ragService.askAIWithAdvisors(
//                """
//                What kind of developer do I want to become?
//                """,
//                userId
//        );

//        String answer2 = ragService.askAIWithAdvisors(
//                """
//                Tell me three things you remember about me.
//                """,
//                userId
//        );

//        System.out.println(
//                "========== SECOND RESPONSE =========="
//        );

//        System.out.println(answer2);
    }
}