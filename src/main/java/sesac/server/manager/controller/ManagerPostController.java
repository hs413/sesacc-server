package sesac.server.manager.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sesac.server.feed.dto.PostListRequest;
import sesac.server.feed.dto.PostListResponse;
import sesac.server.manager.service.ManagerPostService;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("manager/posts")
public class ManagerPostController {

    private final ManagerPostService managerPostService;


    @GetMapping
    public ResponseEntity<Page<PostListResponse>> postList(
            @ModelAttribute PostListRequest request,
//            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
            Pageable pageable
    ) {
        Page<PostListResponse> responses =
                managerPostService.postList(pageable, request, request.postType());

        return ResponseEntity.ok().body(responses);
    }
}
