package spring_ollama.spring_ai_ollama_a.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RAGService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

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
                .advisors(
                        new SimpleLoggerAdvisor()
                )
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
}