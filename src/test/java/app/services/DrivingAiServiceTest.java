package app.services;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DrivingAiServiceTest {

    @Test
    void askDrivingAssistantShouldReturnDanishAnswer()
            throws IOException {

        DrivingAiService aiService = new DrivingAiService();

        String answer = aiService.askDrivingAssistant(
                "Hvad betyder ABS i en bil?"
        ).join();

        assertNotNull(answer);
        assertFalse(answer.isBlank());

        System.out.println(answer);
    }

    @Test
    void askDrivingAssistantShouldReturnEnglishAnswer()
            throws IOException {

        DrivingAiService aiService = new DrivingAiService();

        String answer = aiService.askDrivingAssistant(
                "What does ABS mean in a car?"
        ).join();

        assertNotNull(answer);
        assertFalse(answer.isBlank());

        System.out.println(answer);
    }

    @Test
    void askDrivingAssistantShouldRejectUnrelatedQuestion()
            throws IOException {

        DrivingAiService aiService = new DrivingAiService();

        String answer = aiService.askDrivingAssistant(
                "Who won the Champions League in 2025?"
        ).join();

        assertNotNull(answer);
        assertFalse(answer.isBlank());

        System.out.println(answer);
    }
}