package app.dto;

import java.time.LocalDateTime;

public record BookingResponseDTO(int id, LocalDateTime bookingTime, int studentId, int lessonId){

}