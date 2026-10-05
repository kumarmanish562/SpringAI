package spring_ollama.spring_ai_ollama_a.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class TravellingTools {

    @Tool(description = "Get the current weather information for a city")
    public String getWeather(
            @ToolParam(
                    description = "City name for which to get the weather information",
                    required = true
            )
            String city) {

        return switch (city) {
            case "Delhi" -> "Sunny, 26 Degrees";
            case "Mumbai" -> "Cloudy, 29 Degrees";
            case "Bangalore" -> "Rainy, 24 Degrees";
            case "Chennai" -> "Sunny, 31 Degrees";
            default -> "Weather information is not available for " + city;
        };
    }
}