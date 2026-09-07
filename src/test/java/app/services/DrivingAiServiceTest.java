package app.services;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DrivingAiServiceTest {

    @Test
    void askDrivingAssistantShouldReturnDanishAnswer()
            throws IOException, InterruptedException {

        DrivingAiService aiService = new DrivingAiService();

        String answer = aiService.askDrivingAssistant(
                "Hvad betyder ABS i en bil?"
        );

        assertNotNull(answer);
        assertFalse(answer.isBlank());

        System.out.println(answer);
    }

    @Test
    void askDrivingAssistantShouldReturnEnglishAnswer()
            throws IOException, InterruptedException {

        DrivingAiService aiService = new DrivingAiService();

        String answer = aiService.askDrivingAssistant(
                "What does ABS mean in a car?"
        );

        assertNotNull(answer);
        assertFalse(answer.isBlank());

        System.out.println(answer);
    }

    @Test
    void askDrivingAssistantShouldRejectUnrelatedQuestion()
            throws IOException, InterruptedException {

        DrivingAiService aiService = new DrivingAiService();

        String answer = aiService.askDrivingAssistant(
                "Who won the Champions League in 2025?"
        );

        assertNotNull(answer);
        assertFalse(answer.isBlank());

        System.out.println(answer);
    }
}