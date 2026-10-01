package app.api.routes;

import app.api.BookingController;
import io.javalin.Javalin;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookingRoutes {

    private final BookingController controller;

    public void register(Javalin app) {
        app.get("/api/bookings", controller::getAll);
        app.get("/api/bookings/{id}", controller::getById);
        app.post("/api/bookings", controller::create);
        app.put("/api/bookings/{id}", controller::update);
        app.delete("/api/bookings/{id}", controller::delete);
    }
}