package sesac.server.campus.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sesac.server.campus.dto.request.UpdateCourseRequest;
import sesac.server.common.entity.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String classNumber;

    private String instructorName;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "campus_id")
    private Campus campus;


    public void updateCourse(UpdateCourseRequest updateCourseRequest, Campus newCampus) {
        if (updateCourseRequest.name() != null) {
            this.name = updateCourseRequest.name();
        }
        if (updateCourseRequest.classNumber() != null) {
            this.classNumber = updateCourseRequest.classNumber();
        }
        if (updateCourseRequest.instructorName() != null) {
            this.instructorName = updateCourseRequest.instructorName();
        }
        if (updateCourseRequest.startDate() != null) {
            this.startDate = updateCourseRequest.startDate();
        }
        if (updateCourseRequest.endDate() != null) {
            this.endDate = updateCourseRequest.endDate();
        }
        if (newCampus != null) {
            this.campus = newCampus;
        }
    }
}
