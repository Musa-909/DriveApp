package app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private LocalDateTime lessonTime;

    @Column
    private int durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column
    private LessonType lessonType;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    @OneToMany(mappedBy = "lesson")
    private List<Booking> bookings;

    public Lesson(LocalDateTime lessonTime, int durationMinutes, LessonType lessonType) {
        this.lessonTime = lessonTime;
        this.durationMinutes = durationMinutes;
        this.lessonType = lessonType;
    }
}