package sesac.server.feed.service;

import static org.assertj.core.api.Assertions.assertThat;

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
import sesac.server.feed.dto.PostResponse;
import sesac.server.feed.entity.Post;
import sesac.server.feed.entity.PostType;
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

    Student student1;
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
                .email("test1@example.com")
                .password("1234")
                .role(UserRole.STUDENT)
                .build();

        em.persist(user);

        student1 = Student.builder()
                .user(user)
                .name("김학생")
                .birthDate(LocalDate.parse("19990101", DateTimeFormatter.ofPattern("yyyyMMdd")))
                .firstCourse(course)
                .gender('M')
                .nickname("새싹_1")
                .statusCode(10)
                .build();

        em.persist(student1);

        User user2 = User.builder()
                .email("test2@example.com")
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
    @DisplayName("게시글 작성")
    public void createPostTest() {
        CreatePostRequest request = new CreatePostRequest("제목", "내용", List.of("해시1", "해시2"), null);

        Post created = postService.createPost(student1.getId(), request);
        em.flush();
        em.clear();

        PostResponse post = postService.getPostDetail(created.getId());

        assertThat(post.title()).isEqualTo("제목");
        assertThat(post.content()).isEqualTo("내용");
        assertThat(post.hashtags()).hasSize(2);
    }

    @Test
    @DisplayName("중복 해시코드")
    public void hashcodeTest() {
        CreatePostRequest request1 = new CreatePostRequest("제목", "내용", List.of("해시1", "해시2"), null);
        CreatePostRequest request2 = new CreatePostRequest("제목", "내용", List.of("해시1", "해시2", "해시3"),
                null);

        Post created1 = postService.createPost(student1.getId(), request1);
        Post created2 = postService.createPost(student1.getId(), request2);
        em.flush();
        em.clear();

        PostResponse post1 = postService.getPostDetail(created1.getId());
        PostResponse post2 = postService.getPostDetail(created2.getId());

        assertThat(post1.hashtags()).hasSize(2);
        assertThat(post2.hashtags()).hasSize(3);
    }


    @Test
    @DisplayName("게시글 목록")
    public void postListTest() {
        for (int i = 1; i <= 27; i++) {
            Student student = i % 2 == 0 ? student1 : student2;
            Post post = Post.builder()
                    .title("제목_" + i)
                    .content("내용_" + i)
                    .type(PostType.CAMPUS)
                    .user(student.getUser())
                    .build();

            em.persist(post);
        }

        em.flush();
        em.clear();

        Pageable pageable = PageRequest.of(0, 10);
        PostListRequest request = new PostListRequest(null, null, null);

        List<PostListResponse> list = postService.getPostList(pageable, request, null);

        assertThat(list).hasSize(10);

        for (PostListResponse post : list) {
            log.info(post);
        }
    }

    @Test
    @DisplayName("게시글 상세")
    public void postDetailTest() {
        Post created = Post.builder()
                .title("제목")
                .content("내용")
                .type(PostType.CAMPUS)
                .user(student1.getUser())
                .build();

        em.persist(created);

        PostResponse post = postService.getPostDetail(created.getId());
        log.info(post);
    }


}