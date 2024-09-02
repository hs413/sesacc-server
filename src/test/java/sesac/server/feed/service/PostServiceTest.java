package sesac.server.feed.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import sesac.server.campus.entity.Campus;
import sesac.server.campus.entity.Course;
import sesac.server.feed.dto.CreatePostRequest;
import sesac.server.feed.dto.PostListRequest;
import sesac.server.feed.dto.PostListResponse;
import sesac.server.user.entity.Student;
import sesac.server.user.entity.User;
import sesac.server.user.entity.UserRole;

@SpringBootTest
@Transactional
@Log4j2
class PostServiceTest {

    @Autowired
    private PostService postService;

    @PersistenceContext
    EntityManager em;

    Student student;
    Student student2;

    @BeforeEach
    public void setup() {
        Campus campus = Campus.builder()
                .name("Campus")
                .address("campus address")
                .build();

        em.persist(campus);

        Course course = Course.builder()
                .campus(campus)
                .name("Course")
                .classNumber("course number")
                .instructorName("instructor name")
                .build();

        em.persist(course);

        User user = User.builder()
                .email("test@example.com")
                .password("1234")
                .role(UserRole.STUDENT)
                .build();

        em.persist(user);

        student = Student.builder()
                .user(user)
                .name("김학생")
                .birthDate(LocalDate.parse("19990101", DateTimeFormatter.ofPattern("yyyyMMdd")))
                .firstCourse(course)
                .gender('M')
                .nickname("새싹_1")
                .statusCode(10)
                .build();

        em.persist(student);

        User user2 = User.builder()
                .email("test1@example.com")
                .password("1234")
                .role(UserRole.STUDENT)
                .build();

        em.persist(user2);

        student2 = Student.builder()
                .user(user2)
                .name("이학생")
                .birthDate(LocalDate.parse("19990101", DateTimeFormatter.ofPattern("yyyyMMdd")))
                .firstCourse(course)
                .gender('F')
                .nickname("새싹_2")
                .statusCode(10)
                .build();
        em.persist(student2);
        em.flush();
        em.clear();
    }


    @Test
    @DisplayName("피드 생성")
    public void createPostTest() {
        CreatePostRequest request = new CreatePostRequest("제목", "내용", null, null);

        postService.createPost(student.getId(), request);
    }

    @Test
    @DisplayName("피드 목록")
    public void postListTest() {
        CreatePostRequest request1 = new CreatePostRequest("제목1", "내용1", null, null);
        CreatePostRequest request2 = new CreatePostRequest("제목2", "내용2", null, null);
        CreatePostRequest request3 = new CreatePostRequest("제목3", "내용3", null, null);

        postService.createPost(student.getId(), request1);
        postService.createPost(student.getId(), request2);
        postService.createPost(student2.getId(), request3);
        em.flush();
        em.clear();
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        PostListRequest request = new PostListRequest(null, null, null);

        List<PostListResponse> list = postService.getPostList(pageable, request,
                request.postType());
        log.info("---------------- list {}", list);
    }

}