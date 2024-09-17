package sesac.server.feed.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record PostPopularResponse(
        Long id,
        String title
) {

    @QueryProjection
    public PostPopularResponse {
    }
}
