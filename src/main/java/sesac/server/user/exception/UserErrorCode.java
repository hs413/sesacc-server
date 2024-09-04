package sesac.server.user.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import sesac.server.common.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(NOT_FOUND, "제목은 필수입니다.");

    private final HttpStatus status;
    private final String message;

    public String getCode() {
        return this.name();
    }
}