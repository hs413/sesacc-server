package sesac.server.manager.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sesac.server.feed.dto.PostListRequest;
import sesac.server.feed.dto.PostListResponse;
import sesac.server.feed.entity.PostType;
import sesac.server.feed.repository.PostRepository;

@Log4j2
@Service
@RequiredArgsConstructor
public class ManagerPostService {

    private final PostRepository postRepository;

    public Page<PostListResponse> postList(Pageable pageable, PostListRequest request,
            PostType type) {
        return postRepository.searchPostPage(pageable, request, type);

//        return null;
    }
}
