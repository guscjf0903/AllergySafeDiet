package org.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodes {
    USERSIGNUP_FAILED(400, "회원가입에 실패하였습니다."),
    PASSWORD_FORMAT_FAILED(400, "비밀번호 양식이 틀렸습니다."),
    NOT_FOUND_USER(404, "존재하지 않는 사용자입니다."),
    PASSWORD_DISMATCH(400,"비밀번호가 일치하지 않습니다."),
    NOT_FOUND_LOGINID(404, "존재하지 않는 로그인 아이디입니다."),
    DUPLICATE_DISEASEDATE_ERROR(404, "이미 등록된 날짜의 질병 데이터가 존재합니다."),
    NOT_FOUND_RECIPE(404, "존재하지 않는 레시피입니다."),
    DUPLICATE_EMAIL(400, "이미 등록된 이메일입니다."),
    ERROR_CREATE_CODE(500, "인증코드 생성에 실패하였습니다."),
    INVALID_EMAIL(404, "이메일이 인증되지 않았습니다.");


    private final int status;
    private final String detail;

}

