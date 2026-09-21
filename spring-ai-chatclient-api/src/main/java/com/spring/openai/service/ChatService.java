package com.spring.openai.service;

import com.spring.openai.entity.Tut;

import java.util.List;

// Service interface.
//
// This interface defines the operations that the ChatService layer provides.
// It does NOT contain the actual implementation/logic.
//
// The implementation of this interface is provided by ChatServiceImpl.
//
// Architecture:
//
// Controller
//     ↓
// ChatService (interface)
//     ↓
// ChatServiceImpl (implementation)
//     ↓
// ChatClient
//     ↓
// OpenAI / Ollama
public interface ChatService {

//    String chat(String query);
//
//    Previous version of the chat() method.
//
//    It returned a simple String response from the LLM.
//
//    Example:
//
//    User:
//        "Tell me about Virat Kohli"
//
//    AI:
//        "Virat Kohli is an Indian cricketer..."
//
//    So the method contract was:
//
//        String chat(String query);
//
//    The query is the user's question.
//    The returned String is the AI-generated response.

//    Tut chat(String query);
//
//    Second version of the chat() method.
//
//    Instead of returning a plain String, the AI response was
//    converted into a single Tut Java object.
//
//    Example:
//
//        Tut tutorial = chatService.chat(query);
//
//    This is useful when we want structured AI output
//    instead of plain text.
//
//    Tut represents your application's custom Java entity/model.

    //    Current method.
//
//    This method accepts the user's query as a String
//    and returns a List of Tut objects.
//
//    Example:
//
//        List<Tut> tutorials = chatService.chat(query);
//
//    The AI response is expected to contain multiple tutorial
//    objects, which Spring AI converts into:
//
//        List<Tut>
//
//    This matches the implementation in ChatServiceImpl:
//
//        .entity(new ParameterizedTypeReference<List<Tut>>() {})
//
//    So the complete flow is:
//
//        User Query
//             ↓
//        chat(String query)
//             ↓
//        ChatServiceImpl
//             ↓
//        ChatClient
//             ↓
//        LLM
//             ↓
//        Structured AI Response
//             ↓
//        List<Tut>
//             ↓
//        Controller
//             ↓
//        JSON Response
    List<Tut> chat(String query);
}