package sesac.server.feed.repository.search;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sesac.server.feed.dto.NoticeListRequest;
import sesac.server.feed.dto.NoticeListResponse;
import sesac.server.feed.entity.Notice;
import sesac.server.feed.entity.NoticeType;

public interface NoticeSearch {

    List<NoticeListResponse> searchNotice(Pageable pageable, NoticeListRequest request,
            NoticeType type);

    Page<NoticeListResponse> searchNoticePage(Pageable pageable, NoticeListRequest request,
            NoticeType type);

}
