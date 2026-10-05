package spring_ollama.spring_ai_ollama_a.service;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class AIServiceTests {

    @Autowired
    private AIService aiService;



    // ---------------------------------------------------------
    // Test Ollama Chat
    // ---------------------------------------------------------

    @Test
    public void testGetJoke() {

        String joke = aiService.getJoke("Programmers");

        System.out.println("========== JOKE ==========");

        System.out.println(joke);

        System.out.println("==========================");
    }


    // ---------------------------------------------------------
    // Test Embedding Model
    // ---------------------------------------------------------

    @Test
    public void testEmbedText() {

        float[] embedding =
                aiService.getEmbedding(
                        "This is a big text here"
                );

        System.out.println("========== EMBEDDING ==========");

        System.out.println(
                "Embedding size: " + embedding.length
        );

        for (float value : embedding) {
            System.out.print(value + " ");
        }

        System.out.println();
        System.out.println("===============================");
    }


    // ---------------------------------------------------------
    // Test PGVector Movie Ingestion
    // ---------------------------------------------------------

    @Test
    public void testStoreData() {

        aiService.ingestMovieData();

        System.out.println(
                "Movie data successfully stored in PGVector."
        );
    }

    @Test
    public void testSimilaritySearch() {

        List<Document> results =
                aiService.similaritySearch(
                        "movies about space and astronauts"
                );

        System.out.println("========== SEARCH RESULTS ==========");

        for (Document document : results) {

            System.out.println("Content:");
            System.out.println(document.getText());

            System.out.println("Metadata:");
            System.out.println(document.getMetadata());

            System.out.println("------------------------------------");
        }
    }
}