package spring_ollama.spring_ai_ollama_a.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.model.ChatResponse;

public class TokenUsageAdvisor implements CallAdvisor {

    private static final Logger log =
            LoggerFactory.getLogger(TokenUsageAdvisor.class);

    @Override
    public ChatClientResponse adviseCall(
            ChatClientRequest chatClientRequest,
            CallAdvisorChain callAdvisorChain) {

        // Start measuring execution time
        long startTime = System.currentTimeMillis();

        // Pass request to the next advisor / LLM
        ChatClientResponse advisedResponse =
                callAdvisorChain.nextCall(chatClientRequest);

        // Get the LLM response
        ChatResponse chatResponse =
                advisedResponse.chatResponse();

        // Read token usage information
        if (chatResponse != null
                && chatResponse.getMetadata().getUsage() != null) {

            var usage = chatResponse.getMetadata().getUsage();

            long duration =
                    System.currentTimeMillis() - startTime;

            log.info(
                    "Token Usage: Input={} | Output={} | Total={} | Time={}ms",
                    usage.getPromptTokens(),
                    usage.getCompletionTokens(),
                    usage.getTotalTokens(),
                    duration
            );
        }

        return advisedResponse;
    }

    @Override
    public String getName() {
        return "TokenUsageAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}