package sesac.server.group.repository.search;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sesac.server.group.dto.request.SearchRunningMateRequest;
import sesac.server.group.dto.response.RunningMateMemberListResponse;
import sesac.server.group.dto.response.SearchRunningMateResponse;

public interface SearchRunningMate {

    Page<SearchRunningMateResponse> runningMateSearch(Pageable pageable,
            SearchRunningMateRequest request);

    boolean existsMember(Long runningMateId, Long userId, String phoneNumber);

    List<RunningMateMemberListResponse> runningMateMembers(Long runningMateId);
}
