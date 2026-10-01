package app.api.routes;

import app.api.LessonController;
import io.javalin.Javalin;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LessonRoutes {

    private final LessonController controller;

    public void register(Javalin app) {
        app.get("/api/lessons", controller::getAll);
        app.get("/api/lessons/{id}", controller::getById);
        app.post("/api/lessons", controller::create);
        app.put("/api/lessons/{id}", controller::update);
        app.delete("/api/lessons/{id}", controller::delete);
    }
}