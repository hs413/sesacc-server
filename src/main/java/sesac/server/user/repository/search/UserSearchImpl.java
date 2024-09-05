package sesac.server.user.repository.search;

import static sesac.server.campus.entity.QCampus.campus;
import static sesac.server.campus.entity.QCourse.course;
import static sesac.server.feed.entity.QPost.post;
import static sesac.server.user.entity.QStudent.student;
import static sesac.server.user.entity.QUser.user;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import sesac.server.campus.entity.QCampus;
import sesac.server.campus.entity.QCourse;
import sesac.server.feed.entity.Post;
import sesac.server.user.dto.response.QUserListResponse;
import sesac.server.user.dto.response.UserListResponse;
import sesac.server.user.entity.QStudent;
import sesac.server.user.entity.QUser;
import sesac.server.user.entity.User;

@RequiredArgsConstructor
public class UserSearchImpl implements UserSearch {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserListResponse> searchUser(Pageable pageable) {
        List<UserListResponse> users = queryFactory
                .select(new QUserListResponse(
                        student.name,
                        user.email,
                        campus.name,
                        course.name,
                        user.createdAt
                ))
                .from(user)
                .join(user.student, student)
                .join(student.firstCourse, course)
                .join(course.campus, campus)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(user.id.desc())
                .fetch();

        JPAQuery<User> countQuery = queryFactory
                .select(user)
//                .where(
//                        typeEq(request.postType()),
//                        titleLike(request.keyword())
//                )
                .from(user);

        return PageableExecutionUtils.getPage(users, pageable, countQuery::fetchCount);
    }
}
