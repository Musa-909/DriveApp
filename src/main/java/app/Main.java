package app;

import app.services.DrivingAiService;

public class Main {

    public static void main(String[] args) throws Exception {

        DrivingAiService aiService = new DrivingAiService();

        String answer = aiService.askDrivingAssistant(
                "What does ABS mean in a car?"
        );
        System.out.println(answer);
    }
}