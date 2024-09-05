package sesac.server.manager.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sesac.server.manager.service.ManagerUserService;
import sesac.server.user.dto.response.UserListResponse;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("manager/users")
public class ManagerUserController {

    private final ManagerUserService managerUserService;

    @GetMapping
    public Page<UserListResponse> getUserList(Pageable pageable) {
        return managerUserService.getUserList(pageable);
    }

    @GetMapping("{userId}")
    public void getUserDetail(@PathVariable Long userId) {

    }

    @PutMapping("{userId}")
    public void updateUser(@PathVariable Long userId) {

    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable Long userId) {

    }

    // 승인 요청
    @PatchMapping("{userId}")
    public void patchUser(@PathVariable Long userId) {

    }
}
