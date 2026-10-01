package app.dto;

import app.entities.LessonType;

import java.time.LocalDateTime;

public record LessonResponseDTO(int id, LocalDateTime lessonTime, int durationMinutes, LessonType lessonType, int instructorId){

}