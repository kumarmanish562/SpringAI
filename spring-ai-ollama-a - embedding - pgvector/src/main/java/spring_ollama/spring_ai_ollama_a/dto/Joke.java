package spring_ollama.spring_ai_ollama_a.dto;

public record Joke(
        String text,
        String category,
        Double laughScore,
        Boolean isNSFW
) {

}
