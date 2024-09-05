package sesac.server.user.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sesac.server.user.dto.response.UserListResponse;
import sesac.server.user.entity.User;

public interface UserSearch {

    Page<UserListResponse> searchUser(Pageable pageable);
}
