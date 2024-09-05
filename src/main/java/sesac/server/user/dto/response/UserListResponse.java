package sesac.server.user.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record UserListResponse(
        String name,
        String email,
        String campus,
        String course,
//        Integer statusCode,
        LocalDateTime createAt
) {

    @QueryProjection
    public UserListResponse {
    }
}
