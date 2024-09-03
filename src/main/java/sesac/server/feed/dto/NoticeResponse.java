package sesac.server.feed.dto;

import java.time.LocalDateTime;
import java.util.List;
import sesac.server.feed.entity.Notice;
import sesac.server.feed.entity.Post;

public record NoticeResponse(
        Long id,
        String writer,
        String title,
        String content,
        LocalDateTime createdAt,
        List<String> hashtags,
        String imageUrl,
        Long likesCount,
        Long replyCount,
        String profileImage
//        List<ReplyResponse> replies
) {

    public NoticeResponse(Notice notice/*, List<ReplyResponse> replies*/) {
        this(
                notice.getId(),
                notice.getUser().getStudent().getNickname(),
                notice.getTitle(),
                notice.getContent(),
                notice.getCreatedAt(),
                notice.getHashtags().stream().map(postHashtag -> postHashtag.getHashtag().getName())
                        .toList(),
                notice.getImage(),
                notice.getLikesCount(),
                notice.getReplyCount(),
                notice.getUser().getStudent().getProfileImage()
//                replies
        );
    }
}
