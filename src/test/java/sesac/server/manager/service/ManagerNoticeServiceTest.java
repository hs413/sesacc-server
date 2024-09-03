package sesac.server.manager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import sesac.server.auth.dto.CustomPrincipal;
import sesac.server.campus.entity.Campus;
import sesac.server.campus.entity.Course;
import sesac.server.common.exception.BaseException;
import sesac.server.feed.dto.CreateNoticeRequest;
import sesac.server.feed.dto.NoticeListRequest;
import sesac.server.feed.dto.NoticeListResponse;
import sesac.server.feed.dto.NoticeResponse;
import sesac.server.feed.dto.UpdateNoticeRequest;
import sesac.server.feed.entity.Notice;
import sesac.server.feed.entity.NoticeType;
import sesac.server.feed.exception.PostErrorCode;
import sesac.server.user.entity.Manager;
import sesac.server.user.entity.Student;
import sesac.server.user.entity.User;
import sesac.server.user.entity.UserRole;

@SpringBootTest
@Transactional
@Log4j2
class ManagerNoticeServiceTest {

    @Autowired
    private ManagerNoticeService managerNoticeService;

    @PersistenceContext
    EntityManager em;

    Manager manager;
    Student student;

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
                .email("manager@example.com")
                .password("1234")
                .role(UserRole.MANAGER)
                .build();

        em.persist(user);

        manager = Manager.builder()
                .user(user)
                .campusName("영등포 캠퍼스")
                .address("영등포")
                .build();

        em.persist(manager);

        User user2 = User.builder()
                .email("test1@example.com")
                .password("1234")
                .role(UserRole.STUDENT)
                .build();

        em.persist(user2);

        student = Student.builder()
                .user(user2)
                .name("김학생")
                .birthDate(LocalDate.parse("19990101", DateTimeFormatter.ofPattern("yyyyMMdd")))
                .firstCourse(course)
                .gender('M')
                .nickname("새싹_1")
                .statusCode(10)
                .build();

        em.persist(student);

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("공지 작성")
    public void createNoticeTest() {
        // give
        CreateNoticeRequest request = new CreateNoticeRequest(
                "공지 제목",
                "내용",
                null,
                List.of("해시1", "해시2"),
                NoticeType.CAMPUS,
                0);

        // when
        Notice created = managerNoticeService.createNotice(manager.getId(), request);
        em.flush();
        em.clear();

        Notice notice = em.find(Notice.class, created.getId());
        assertThat(notice.getTitle()).isEqualTo("공지 제목");
        assertThat(notice.getContent()).isEqualTo("내용");
        assertThat(notice.getHashtags()).hasSize(2);
        assertThat(notice.getType()).isEqualTo(NoticeType.CAMPUS);
    }


    @Test
    @DisplayName("중복 해시코드")
    public void hashcodeTest() {
        // give
        CreateNoticeRequest request1 = new CreateNoticeRequest("공지 제목", "내용", "",
                List.of("해시1", "해시2"), NoticeType.CAMPUS, 0);
        CreateNoticeRequest request2 = new CreateNoticeRequest("공지 제목", "내용", "",
                List.of("해시1", "해시2", "해시3"), NoticeType.CAMPUS, 0);

        // when
        Notice created1 = managerNoticeService.createNotice(manager.getId(), request1);
        Notice created2 = managerNoticeService.createNotice(manager.getId(), request2);

        em.flush();
        em.clear();

        // then
        Notice post1 = em.find(Notice.class, created1.getId());
        Notice post2 = em.find(Notice.class, created2.getId());

        assertThat(post1.getHashtags()).hasSize(2);
        assertThat(post2.getHashtags()).hasSize(3);
    }


    @Test
    @DisplayName("공지 목록")
    public void postListTest() {
        // give
        for (int i = 1; i <= 8; i++) {
            Notice notice = Notice.builder()
                    .title("공지 제목_" + i)
                    .content("내용_" + i)
                    .type(NoticeType.CAMPUS)
                    .status(true)
                    .importance(0)
                    .user(manager.getUser())
                    .build();

            em.persist(notice);
        }

        em.flush();
        em.clear();

        Pageable pageable1 = PageRequest.of(0, 10);
        NoticeListRequest request = new NoticeListRequest(null, null, null);

        // when
        Page<NoticeListResponse> list = managerNoticeService.getNoticeList(pageable1, request,
                null);

        // then
        assertThat(list).hasSize(8);
        List<NoticeListResponse> notices = list.getContent();

        for (NoticeListResponse post : notices) {
            log.info(post);
        }
    }

    @Test
    @DisplayName("공지 상세")
    public void postDetailTest() {
        // give
        Notice created = Notice.builder()
                .title("공지 제목")
                .content("내용")
                .type(NoticeType.CAMPUS)
                .user(manager.getUser())
                .build();

        em.persist(created);
        em.flush();
        em.clear();

        // when
        NoticeResponse post = managerNoticeService.getNotice(created.getId());

        // then
        assertThat(post.title()).isEqualTo("공지 제목");
        assertThat(post.content()).isEqualTo("내용");
        assertThat(post.writer()).isEqualTo(manager.getCampusName());
    }

    @Test
    @DisplayName("공지 수정")
    public void postUpdateTest() {
        // give
        Notice created = Notice.builder()
                .title("공지 제목")
                .content("내용")
                .type(NoticeType.CAMPUS)
                .user(manager.getUser())
                .importance(0)
                .build();

        em.persist(created);
        em.flush();
        em.clear();

        UpdateNoticeRequest request = new UpdateNoticeRequest("수정 공지 제목", "수정 내용", NoticeType.ALL,
                1);

        // when
        managerNoticeService.updateNotice(created.getId(), request);

        // then
        Notice notice = em.find(Notice.class, created.getId());
        assertThat(notice.getTitle()).isEqualTo("수정 공지 제목");
        assertThat(notice.getContent()).isEqualTo("수정 내용");
        assertThat(notice.getImportance()).isEqualTo(1);
        assertThat(notice.getType()).isEqualTo(NoticeType.ALL);
    }


    @Test
    @DisplayName("공지 삭제")
    public void postDeleteTest() {
        // give
        Notice created = Notice.builder()
                .title("공지 제목")
                .content("내용")
                .type(NoticeType.CAMPUS)
                .user(manager.getUser())
                .build();

        em.persist(created);
        em.flush();
        em.clear();

        CustomPrincipal principal = new CustomPrincipal(manager.getId(), "MANAGER");

        // when
        managerNoticeService.deleteNotice(created.getId());

        // then
        BaseException ex = assertThrows(BaseException.class,
                () -> managerNoticeService.deleteNotice(created.getId()));
        assertThat(ex.getErrorCode()).isEqualTo(PostErrorCode.NO_POST);
    }


}