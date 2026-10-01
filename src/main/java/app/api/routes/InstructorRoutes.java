package app.api.routes;

import app.api.InstructorController;
import io.javalin.Javalin;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InstructorRoutes {

    private final InstructorController controller;

    public void register(Javalin app) {
        app.get("/api/instructors", controller::getAll);
        app.get("/api/instructors/{id}", controller::getById);
        app.post("/api/instructors", controller::create);
        app.put("/api/instructors/{id}", controller::update);
        app.delete("/api/instructors/{id}", controller::delete);
    }
}