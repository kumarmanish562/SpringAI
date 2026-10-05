package spring_ollama.spring_ai_ollama_a.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AIServiceTests {

    @Autowired
    private AIService aiService;

    @Test
    public void testGetJoke() {

        var joke = aiService.getJoke("Programmers");

        System.out.println(joke);
    }

    @Test
    public void testEmbedText() {

        var embed = aiService.getEmbedding(
                "This is a big text here"
        );

        System.out.println("Embedding size: " + embed.length);

        for (float e : embed) {
            System.out.print(e + " ");
        }
    }
}