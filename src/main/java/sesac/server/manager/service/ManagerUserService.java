package sesac.server.manager.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sesac.server.user.dto.response.UserListResponse;
import sesac.server.user.repository.UserRepository;


@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class ManagerUserService {

    private final UserRepository userRepository;

    public Page<UserListResponse> getUserList(Pageable pageable) {

        return userRepository.searchUser(pageable);

//        page: int
//pageSize: int
//course: string
//status: string
//approved: int

    }

//    public void getUserDetail(Long userId) {
//
//    }
//
//    public void updateUser(Long userId) {
//
//    }
//
//    public void deleteUser(Long userId) {
//
//    }
//
//    // 승인 요청
//    public void patchUser(Long userId) {
//
//    }
}
