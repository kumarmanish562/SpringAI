package spring_ollama.spring_ai_ollama_a.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spring_ollama.spring_ai_ollama_a.tool.TravellingTools;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient chatClient;
    private final TravellingTools travellingTools;

    @PostMapping("/chat")
    public String chat(@RequestBody String message) {

        if (message.toLowerCase().contains("weather")) {

            return travellingTools.getWeather("Delhi");
        }

        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}