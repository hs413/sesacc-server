package sesac.server.feed.repository.search;

import static org.springframework.util.StringUtils.hasText;
import static sesac.server.feed.entity.QPost.post;
import static sesac.server.user.entity.QUser.user;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import sesac.server.feed.dto.PostListRequest;
import sesac.server.feed.dto.PostListResponse;
import sesac.server.feed.entity.Post;
import sesac.server.feed.entity.PostType;

@RequiredArgsConstructor
public class PostSearchImpl implements PostSearch {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostListResponse> searchPost(
            Pageable pageable,
            PostListRequest request,
            PostType type
    ) {

        List<Post> postList = queryFactory
                .selectFrom(post)
                .join(post.user, user)
                .where(
                        typeEq(request.postType())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.id.desc())
                .fetch();

        List<PostListResponse> posts = postList.stream().map(PostListResponse::new).toList();

        return posts;
    }

    @Override
    public Page<PostListResponse> searchPostPage(
            Pageable pageable,
            PostListRequest request,
            PostType type
    ) {

        List<PostListResponse> posts = this.searchPost(pageable, request, type);

        JPAQuery<Post> countQuery = queryFactory
                .select(post)
                .where(
                        typeEq(request.postType())
                )
                .from(post);

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchCount);
    }

//    private BooleanExpression usernameEq(String keyword) {
//        return hasText(keyword) ? member.username.eq(username) : null;
//    }

    private BooleanExpression typeEq(PostType type) {
        return type != null ? post.type.eq(type) : null;
    }

//    private BooleanExpression ageGoe(Integer ageGoe) {
//        return ageGoe != null ? member.age.goe(ageGoe) : null;
//    }
//
//    private BooleanExpression ageLoe(Integer ageLoe) {
//        return ageLoe != null ? member.age.goe(ageLoe) : null;
//    }

}
