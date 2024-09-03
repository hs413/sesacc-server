package sesac.server.campus.service;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sesac.server.campus.dto.CourseResponse;
import sesac.server.campus.entity.Campus;
import sesac.server.campus.entity.Course;
import sesac.server.campus.repository.CampusRepository;
import sesac.server.campus.repository.CourseRepository;

@Log4j2
@SpringBootTest
@Transactional
class CourseServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CampusRepository campusRepository;

    private Long campusId;

    @BeforeEach
    public void before() {
        campusId = campusRepository.findAll().get(0).getId();
    }

    /*@Test
    @DisplayName("전체 조회 테스트")
    public void findAll() {
        List<CourseResponse> list = courseService.findAll(campusId);

        list.forEach(log::info);
        assertThat(list).isNotEmpty();
        assertThat(list).hasSize(3);
    }*/
}