package sesac.server.account.exception;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import sesac.server.common.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum AccountErrorCode implements ErrorCode {
    DUPLICATED_EMAIL(BAD_REQUEST, "이미 가입된 이메일 입니다"),
    REQUIRED_EMAIL(BAD_REQUEST, "이메일 주소를 입력해 주세요"),
    INVALID_EMAIL_PATTERN(BAD_REQUEST, "이메일 형식이 아닙니다"),
    REQUIRED_PASSWORD(BAD_REQUEST, "비밀번호를 입력해 주세요"),
    INVALID_PASSWORD_PATTERN(BAD_REQUEST, "비밀번호는 8~20자 이내로 영어, 숫자, 특수문자를 포함해야 합니다"),
    REQUIRED_PASSWORD_CONFIRM(BAD_REQUEST, "비밀번호 확인이 입력되지 않았습니다"),
    DIFFERENT_PASSWORD_CONFIRM(BAD_REQUEST, "비밀번호 확인이 일치하지 않습니다"),
    REQUIRED_NAME(BAD_REQUEST, "이름이 입력되지 않았습니다"),
    INVALID_NAME_PATTERN(BAD_REQUEST, "이름은 한글만 입력 가능합니다"),
    INVALID_NAME_SIZE(BAD_REQUEST, "이름은 1 ~ 5자로 입력해주세요"),
    REQUIRED_BIRTH(BAD_REQUEST, "주민번호가 입력되지 않았습니다"),
    INVALID_BIRTH_PATTERN(BAD_REQUEST, "주민번호는 숫자만 입력 가능합니다"),
    INVALID_BIRTH_SIZE(BAD_REQUEST, "주민번호 앞자리를 올바르게 입력해주세요"),
    REQUIRED_GENDER(BAD_REQUEST, "주민번호가 입력되지 않았습니다"),
    INVALID_GENDER(BAD_REQUEST, "주민번호 뒷자리는 1 ~ 4의 값을 입력해주세요"),
    REQUIRED_COURSE(BAD_REQUEST, "과정을 선택해주세요"),
    NO_COURSE(BAD_REQUEST, "과정이 없습니다"),

    NO_EMAIL_OR_PASSWORD(BAD_REQUEST,
            "아이디 또는 비밀번호가 잘못되었습니다. 다시 입력해 주세요"),

    PENDING_ACCOUNT(UNAUTHORIZED, "아직 승인되지 않은 회원입니다."),
    //    APPROVED_ACCOUNT(HttpStatus.BAD_REQUEST, "", ""),
    HOLD_ACCOUNT(UNAUTHORIZED, "승인 보류된 회원입니다."),
    REJECTED_ACCOUNT(UNAUTHORIZED, "승인 거절된 회원입니다."),
    ;

    private final HttpStatus status;
    private final String message;

    @Override
    public String getCode() {
        return name();
    }
}
