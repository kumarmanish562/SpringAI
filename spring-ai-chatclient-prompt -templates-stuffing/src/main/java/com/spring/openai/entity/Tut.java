package com.spring.openai.entity;



// This class represents the structure of one tutorial object.
//
// Spring AI can use this class to convert the AI's structured response
// into a Java object.
//
// For example, the AI may return:
//
// {
//     "title": "Spring Boot",
//     "content": "Spring Boot simplifies Spring application development.",
//     "createdYear": "2014"
// }
//
// Spring AI can map that response to:
//
// Tut tutorial = new Tut(
//     "Spring Boot",
//     "Spring Boot simplifies Spring application development.",
//     "2014"
// );
public class Tut {

    // Stores the title/name of the tutorial.
    //
    // Example:
    // "Spring Boot"
    // "Spring AI"
    // "Spring Security"
    private String title;

    // Stores the main content or description of the tutorial.
    //
    // Example:
    // "Spring Boot is a framework used to build Java applications."
    private String content;

    // Stores the year in which the tutorial/topic was created.
    //
    // It is declared as String instead of Integer because
    // the AI response is expected to provide this value as text.
    //
    // Example:
    // "2014"
    private String createdYear;


    // No-argument constructor.
    //
    // This constructor is important for object creation and
    // deserialization frameworks such as Jackson.
    //
    // Spring AI uses structured output conversion, where the
    // JSON-like AI response is converted into a Java object.
    //
    // Having a default constructor makes it easier for the
    // deserialization process to create the object first and
    // then populate its fields.
    public Tut() {
    }

    // Parameterized constructor.
    //
    // This constructor allows us to create a Tut object
    // by providing all three values at once.
    //
    // Example:
    //
    // Tut tutorial = new Tut(
    //     "Spring Boot",
    //     "Spring Boot simplifies Java development.",
    //     "2014"
    // );
    public Tut(String title, String content, String createdYear) {
        this.title = title;
        this.content = content;
        this.createdYear = createdYear;
    }


    // Getter for title.
    //
    // Used to retrieve the value of the title field.
    public String getTitle() {
        return title;
    }

    // Setter for title.
    //
    // Used to assign a value to the title field.
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter for content.
    //
    // Used to retrieve the value of the content field.
    public String getContent() {
        return content;
    }

    // Setter for content.
    //
    // Used to assign a value to the content field.
    public void setContent(String content) {
        this.content = content;
    }

    // Getter for createdYear.
    //
    // Used to retrieve the value of the createdYear field.
    public String getCreatedYear() {
        return createdYear;
    }

    // Setter for createdYear.
    //
    // Used to assign a value to the createdYear field.
    public void setCreatedYear(String createdYear) {
        this.createdYear = createdYear;
    }
}