package sesac.server.campus.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sesac.server.auth.dto.CustomPrincipal;
import sesac.server.campus.dto.request.CreateCourseRequest;
import sesac.server.campus.dto.request.UpdateCourseRequest;
import sesac.server.campus.dto.response.CourseDetailResponse;
import sesac.server.campus.dto.response.CourseResponse;
import sesac.server.campus.dto.response.ExtendedCourseResponse;
import sesac.server.campus.entity.Campus;
import sesac.server.campus.entity.Course;
import sesac.server.campus.exception.CampusErrorCode;
import sesac.server.campus.exception.CourseErrorCode;
import sesac.server.campus.repository.CampusRepository;
import sesac.server.campus.repository.CourseRepository;
import sesac.server.common.dto.PageResponse;
import sesac.server.common.exception.BaseException;
import sesac.server.user.entity.Manager;
import sesac.server.user.exception.UserErrorCode;
import sesac.server.user.repository.ManagerRepository;

@Service
@Log4j2
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CampusRepository campusRepository;
    private final ManagerRepository managerRepository;
    private final CourseUpdateValidationService updateValidationService;

    public List<CourseResponse> findAll(Long campusId) {
        List<Course> list = courseRepository.findByCampusId(campusId);

        List<CourseResponse> response =
                list.stream().map(r -> campusToResponse(r)).toList();

        return response;
    }

    public PageResponse<ExtendedCourseResponse> getCourseList(
            CustomPrincipal principal,
            Pageable pageable,
            String status
    ) {

        Page<ExtendedCourseResponse> courses = courseRepository.searchCourse(principal.id(),
                pageable, status);

        return new PageResponse<>(courses);
    }

    public void createCourse(Long campusId, CreateCourseRequest request) {
        Campus campus = (Campus) getEntity("campus", campusId);

        Course course = Course.builder()
                .name(request.name())
                .classNumber(request.classNumber())
                .instructorName(request.instructorName())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .campus(campus)
                .build();

        courseRepository.save(course);
    }

    public void updateCourse(CustomPrincipal principal, Long campusId, Long courseId,
            UpdateCourseRequest request) {

        Course course = (Course) getEntity("course", courseId);// 코스 존재 여부 확인

        if (!hasCoursePermssion(course, principal)) { // 권한 검사
            throw new BaseException(CourseErrorCode.NO_PERMISSION);
        }

        UpdateCourseRequest validatedRequest = updateValidationService
                .validateAndPrepareUpdate(request, course);  // 유효성 검사 및 업데이트 준비

        Campus newCampus = null;
        if (validatedRequest.newCampusId()
                != null) {                                  // 새 캠퍼스 ID가 제공된 경우 해당 캠퍼스 조회
            newCampus = (Campus) getEntity("campus", validatedRequest.newCampusId());
        }
        course.updateCourse(validatedRequest, newCampus);  // 코스 업데이트

        courseRepository.save(course);
    }

    public CourseDetailResponse getCourseDetail(Long courseId) {
        Course course = (Course) getEntity("course", courseId);
        return CourseDetailResponse.from(course);
    }

    public void deleteCourse(CustomPrincipal principal, Long campusId, Long courseId) {

        Course course = (Course) getEntity("course", courseId);// 코스 존재 여부 확인

        if (!hasCoursePermssion(course, principal)) { // 권한 검사
            throw new BaseException(CourseErrorCode.NO_PERMISSION);
        }

        courseRepository.delete(course);
    }

    private CourseResponse campusToResponse(Course course) {
        return new CourseResponse(course.getId(), course.getName(), course.getClassNumber());
    }

    private boolean hasCoursePermssion(Course course, CustomPrincipal principal) {
        Manager manager = (Manager) getEntity("manager", principal.id());

        Long courseCampusId = course.getCampus().getId();
        Long managerCampusId = manager.getCampus().getId();

        return courseCampusId.equals(managerCampusId);
    }

    private Object getEntity(String entityType, Long id) {
        return switch (entityType) {
            case "campus" -> campusRepository.findById(id).orElseThrow(
                    () -> new BaseException(CampusErrorCode.NO_CAMPUS)
            );
            case "course" -> courseRepository.findById(id).orElseThrow(
                    () -> new BaseException(CourseErrorCode.NO_COURSE)
            );
            case "manager" -> managerRepository.findById(id).orElseThrow(
                    () -> new BaseException(UserErrorCode.NO_USER)
            );
            default -> null;
        };
    }
}
