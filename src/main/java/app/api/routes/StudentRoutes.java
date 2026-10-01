package app.api.routes;

import app.api.StudentController;
import io.javalin.Javalin;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudentRoutes {

    private final StudentController controller;

    public void register(Javalin app) {
        app.get("/api/students", controller::getAll);
        app.get("/api/students/{id}", controller::getById);
        app.post("/api/students", controller::create);
        app.put("/api/students/{id}", controller::update);
        app.delete("/api/students/{id}", controller::delete);
    }
}