package sesac.server.feed.dto;

import sesac.server.feed.entity.NoticeType;
import sesac.server.feed.entity.PostType;

public record NoticeListRequest(
        String keyword,
        Boolean reported,
        NoticeType type
) {

}
