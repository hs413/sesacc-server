package sesac.server.manager.controller;

import static sesac.server.feed.exception.PostErrorCode.*;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sesac.server.auth.dto.AuthPrincipal;
import sesac.server.auth.dto.CustomPrincipal;
import sesac.server.common.exception.BindingResultHandler;
import sesac.server.feed.dto.CreateNoticeRequest;
import sesac.server.feed.exception.PostErrorCode;
import sesac.server.manager.service.ManagerNoticeService;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("manager/notices")
public class ManagerNoticeController {

    private final ManagerNoticeService noticeService;
    private final BindingResultHandler bindingResultHandler;

    @PostMapping
    public ResponseEntity<Void> createNotice(
            @AuthPrincipal CustomPrincipal principal,
            @Valid @RequestBody CreateNoticeRequest request,
            BindingResult bindingResult
    ) {
        bindingResultHandler.handleBindingResult(bindingResult, List.of(
                REQUIRED_TITLE,
                INVALID_TITLE_SIZE,
                REQUIRED_CONTENT,
                INVALID_CONTENT_SIZE,
                REQUIRED_NOTICE_TYPE
        ));

        noticeService.createNotice(principal.id(), request);

        return ResponseEntity.ok().build();
    }
}
