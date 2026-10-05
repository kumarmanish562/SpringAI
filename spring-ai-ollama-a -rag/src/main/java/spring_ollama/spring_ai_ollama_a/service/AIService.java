package spring_ollama.spring_ai_ollama_a.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import spring_ollama.spring_ai_ollama_a.dto.Joke;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AIService {

    private final ChatClient chatClient;
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;


    // ---------------------------------------------------------
    // Generate Embedding
    // ---------------------------------------------------------

    public float[] getEmbedding(String text) {
        return embeddingModel.embed(text);
    }


    //
    public String askAi(String prompt) {

        String template = """
            You are an AI assistant helping a developer.

            Rules:
            - Use ONLY the information provided in the context.
            - You may rephrase, summarize, and explain the information.
            - Do NOT introduce new concepts or facts.
            - If multiple contexts answer the question, combine them.
            - If the answer is not present, say "I don't know."

            Context:
            {context}

            Question:
            {question}

            Answer in a friendly, conversational tone.
            """;

        // 1. Search relevant documents from PGVector
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(prompt)
                        .topK(2)
                        .filterExpression("topic == 'AI' || topic == 'VectorStore'")
                        .build()
        );

        // 2. Convert retrieved documents into context
        String context = documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));

        // 3. Create final prompt
        PromptTemplate promptTemplate =
                new PromptTemplate(template);

        String renderedPrompt = promptTemplate.render(
                Map.of(
                        "context", context,
                        "question", prompt
                )
        );

        // 4. Send augmented prompt to Ollama
        return chatClient
                .prompt()
                .user(renderedPrompt)
                .advisors(
                        new SimpleLoggerAdvisor()
                )
                .call()
                .content();
    }
    public List<Document> springAiDocs() {

        return List.of(

                new Document(
                        "Spring AI provides abstractions like ChatClient, ChatModel, and EmbeddingModel to interact with AI models.",
                        Map.of(
                                "topic", "AI"
                        )
                ),

                new Document(
                        "VectorStore is used to store embeddings and perform similarity searches for relevant information.",
                        Map.of(
                                "topic", "VectorStore"
                        )
                ),

                new Document(
                        "Retrieval Augmented Generation combines vector similarity search with language models to provide context-aware answers.",
                        Map.of(
                                "topic", "RAG"
                        )
                ),

                new Document(
                        "Spring AI supports different AI model providers including Ollama, OpenAI, and other providers.",
                        Map.of(
                                "topic", "Spring AI"
                        )
                ),

                new Document(
                        "ChatClient provides a fluent API to send prompts to language models and receive responses.",
                        Map.of(
                                "topic", "ChatClient"
                        )
                )
        );
    }


    // ---------------------------------------------------------
    // Store Movie Data in PGVector
    // ---------------------------------------------------------

    public void ingestMovieData() {

        List<Document> movies = List.of(

                new Document(
                        """
                        Movie: Inception
                        Genre: Science Fiction, Thriller
                        Director: Christopher Nolan
                        Year: 2010
                        Description: A skilled thief who steals secrets through dreams
                        is given a chance to have his criminal record erased by performing inception.
                        """,
                        Map.of(
                                "movie", "Inception",
                                "genre", "Science Fiction",
                                "director", "Christopher Nolan",
                                "year", 2010
                        )
                ),

                new Document(
                        """
                        Movie: Interstellar
                        Genre: Science Fiction, Drama
                        Director: Christopher Nolan
                        Year: 2014
                        Description: A group of astronauts travels through a wormhole
                        to find a new home for humanity.
                        """,
                        Map.of(
                                "movie", "Interstellar",
                                "genre", "Science Fiction",
                                "director", "Christopher Nolan",
                                "year", 2014
                        )
                ),

                new Document(
                        """
                        Movie: The Dark Knight
                        Genre: Action, Crime, Drama
                        Director: Christopher Nolan
                        Year: 2008
                        Description: Batman faces the Joker, a criminal mastermind
                        who creates chaos across Gotham City.
                        """,
                        Map.of(
                                "movie", "The Dark Knight",
                                "genre", "Action",
                                "director", "Christopher Nolan",
                                "year", 2008
                        )
                ),

                new Document(
                        """
                        Movie: The Matrix
                        Genre: Science Fiction, Action
                        Director: The Wachowskis
                        Year: 1999
                        Description: A computer programmer discovers that reality
                        is actually a simulated world controlled by machines.
                        """,
                        Map.of(
                                "movie", "The Matrix",
                                "genre", "Science Fiction",
                                "director", "The Wachowskis",
                                "year", 1999
                        )
                ),

                new Document(
                        """
                        Movie: Avengers Endgame
                        Genre: Action, Superhero, Science Fiction
                        Director: Anthony Russo, Joe Russo
                        Year: 2019
                        Description: The Avengers attempt to reverse the devastating
                        events caused by Thanos and restore the universe.
                        """,
                        Map.of(
                                "movie", "Avengers Endgame",
                                "genre", "Superhero",
                                "director", "Anthony Russo, Joe Russo",
                                "year", 2019
                        )
                ),

                new Document(
                        """
                        Movie: 3 Idiots
                        Genre: Comedy, Drama
                        Director: Rajkumar Hirani
                        Year: 2009
                        Description: Three engineering students navigate college life,
                        friendship, academic pressure, and their dreams.
                        """,
                        Map.of(
                                "movie", "3 Idiots",
                                "genre", "Comedy",
                                "director", "Rajkumar Hirani",
                                "year", 2009
                        )
                ),

                new Document(
                        """
                        Movie: Dangal
                        Genre: Biography, Drama, Sports
                        Director: Nitesh Tiwari
                        Year: 2016
                        Description: A former wrestler trains his daughters to become
                        successful professional wrestlers despite social challenges.
                        """,
                        Map.of(
                                "movie", "Dangal",
                                "genre", "Sports",
                                "director", "Nitesh Tiwari",
                                "year", 2016
                        )
                ),

                new Document(
                        """
                        Movie: Bahubali The Beginning
                        Genre: Action, Drama, Epic
                        Director: S. S. Rajamouli
                        Year: 2015
                        Description: A young man discovers his royal heritage
                        and becomes involved in a battle for an ancient kingdom.
                        """,
                        Map.of(
                                "movie", "Bahubali The Beginning",
                                "genre", "Action",
                                "director", "S. S. Rajamouli",
                                "year", 2015
                        )
                )
        );

        vectorStore.add(movies);
        vectorStore.add(springAiDocs());
    }


    // ---------------------------------------------------------
    // Similarity Search
    // ---------------------------------------------------------

    public List<Document> similaritySearch(String text) {

        return vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(text)
                        .topK(3)
                        .similarityThreshold(0.3)
                        .build()
        );
    }


    // ---------------------------------------------------------
    // Generate Joke using Ollama
    // ---------------------------------------------------------

    public String getJoke(String topic) {

        String systemPrompt = """
                You are a sarcastic joker, you make poetic jokes in 4 lines.
                You don't make jokes about politics.
                Give a joke on the topic: {topic}
                """;

        PromptTemplate promptTemplate =
                new PromptTemplate(systemPrompt);

        String renderedText =
                promptTemplate.render(
                        Map.of("topic", topic)
                );

        Joke response = chatClient
                .prompt()
                .user(renderedText)
                .advisors(
                        new SimpleLoggerAdvisor()
                )
                .call()
                .entity(Joke.class);

        return response.text();
    }
}