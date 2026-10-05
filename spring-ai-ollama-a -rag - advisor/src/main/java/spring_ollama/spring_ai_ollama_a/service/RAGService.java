package spring_ollama.spring_ai_ollama_a.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import spring_ollama.spring_ai_ollama_a.advisor.TokenUsageAdvisor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RAGService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final ChatMemory chatMemory;

    @Value("classpath:faq.pdf")
    private Resource pdfFile;



    // ---------------------------------------------------------
    // RAG - Ask AI
    // ---------------------------------------------------------

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


        // 1. Search relevant PDF chunks from PGVector
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(prompt)
                        .topK(4)
                        .similarityThreshold(0.4)
                        .filterExpression("file_name == 'faq.pdf'")
                        .build()
        );


        // 2. Convert documents into context
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


        // 4. Send context + question to Ollama
        return chatClient
                .prompt()
                .user(renderedPrompt)
                .advisors()
                .call()
                .content();
    }


    // ---------------------------------------------------------
    // PDF → Documents → Chunks → PGVector
    // ---------------------------------------------------------

    public void ingestPdfToVectorStore() {

        // 1. Read PDF
        PagePdfDocumentReader reader =
                new PagePdfDocumentReader(pdfFile);

        List<Document> pages = reader.get();


        // 2. Split PDF pages into smaller chunks
        TokenTextSplitter tokenTextSplitter =
                TokenTextSplitter.builder()
                        .withChunkSize(200)
                        .build();


        List<Document> chunks =
                tokenTextSplitter.apply(pages);


        // 3. Store chunks in PGVector
        vectorStore.add(chunks);
    }

    public String askAIWithAdvisors(String prompt, String userId) {

        return chatClient.prompt()
                .system("""
                    You are a professional AI assistant.

                    Your responsibilities:
                    - Provide accurate, clear, and concise responses.
                    - Understand the user's question before responding.
                    - Use relevant conversation context when available.
                    - Use the provided PDF knowledge when answering questions about the document.
                    - Explain technical concepts in a structured and easy-to-understand manner.
                    - Do not invent or assume information.
                    - If the required information is unavailable, clearly state that you do not have enough information.
                    - Maintain a professional, respectful, and helpful tone.
                    """)
                .user(prompt)

                //                .advisors(
//                        VectorStoreChatMemoryAdvisor.builder(vectorStore)
//                                .build()
//                )
//                .advisors(a -> a.param(
//                        ChatMemory.CONVERSATION_ID,
//                        userId
//                ))

                .advisors(


//                        new SafeGuardAdvisor(List.of("Politics", "Gaming")),

                        new TokenUsageAdvisor(),



                        // Conversation memory
                        MessageChatMemoryAdvisor.builder(chatMemory)
                                .build(),

                        // PDF RAG
                        QuestionAnswerAdvisor.builder(vectorStore)
                                .searchRequest(
                                        SearchRequest.builder()
                                                .filterExpression("file_name == 'faq.pdf'")
                                                .topK(4)
                                                .build()
                                )
                                .build()
                )

                // Conversation ID
                .advisors(a -> a.param(
                        ChatMemory.CONVERSATION_ID,
                        userId
                ))

                .call()
                .content();
    }


}

