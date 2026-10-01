package app.dto;

import app.entities.LessonType;

import java.time.LocalDateTime;

public record LessonRequestDTO(LocalDateTime lessonTime, int durationMinutes, LessonType lessonType, int instructorId){

}