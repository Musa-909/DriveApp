package app.services;

import app.dto.GeminiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class DrivingAiService {

    private static final String API_KEY = System.getenv("GEMINI_API_KEY");
    private static final String MODEL = "gemini-3.5-flash-lite";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/" + MODEL + ":generateContent";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String askDrivingAssistant(String question)
            throws IOException, InterruptedException {

        if (API_KEY == null || API_KEY.isBlank()) {
            throw new IllegalStateException("GEMINI API KEY is not found");
        }

        String prompt = """
                You are an AI assistant for a Danish driving school.

                Only answer questions related to driving, driving licences,
                driving theory, traffic rules, road signs, road safety,
                cars, vehicle knowledge and driving lessons.

                If the question is unrelated to these topics, explain that
                you can only help with driving school related questions.

                Answer in Danish when the question is in Danish.
                Answer in English when the question is in English.

                Question:
                """ + question;

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        String jsonBody = objectMapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("x-goog-api-key", API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Gemini API error, HTTP status: " + response.statusCode());
        }

        GeminiResponse geminiResponse = objectMapper.readValue(response.body(), GeminiResponse.class);

        return geminiResponse
                .candidates()
                .get(0)
                .content()
                .parts()
                .get(0)
                .text();
    }
}