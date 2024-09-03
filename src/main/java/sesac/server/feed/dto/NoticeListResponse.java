package sesac.server.feed.dto;

import java.time.LocalDateTime;
import java.util.List;
import sesac.server.feed.entity.Notice;
import sesac.server.feed.entity.NoticeType;
import sesac.server.feed.entity.Post;

public record NoticeListResponse(
        Long id,
        String writer,
        String title,
        String content,
        LocalDateTime createdAt,
        String imageUrl,
        Long likesCount,
        Long replyCount,
        List<String> tags

) {

    public NoticeListResponse(Notice notice) {
        this(
                notice.getId(),
                notice.getUser().getStudent().getNickname(),
                notice.getTitle(),
                notice.getContent(),
                notice.getCreatedAt(),
                notice.getImage(),
                notice.getLikesCount(),
                notice.getReplyCount(),
                notice.getHashtags().stream().map(r -> r.getHashtag().getName()).toList()
        );
    }
}
