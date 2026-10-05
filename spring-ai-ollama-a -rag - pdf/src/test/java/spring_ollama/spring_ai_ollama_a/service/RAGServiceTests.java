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
}