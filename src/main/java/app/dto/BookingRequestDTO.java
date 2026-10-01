package app.dto;

import java.time.LocalDateTime;

public record BookingRequestDTO(LocalDateTime bookingTime, int studentId, int lessonId){

}