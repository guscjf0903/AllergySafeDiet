package org.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodes {
    USERSIGNUP_FAILED(400, "회원가입에 실패하였습니다."),
    PASSWORD_FORMAT_FAILED(400, "비밀번호 양식이 틀렸습니다."),
    NOT_FOUND_USER(400, "존재하지 않는 사용자입니다."),
    PASSWORD_DISMATCH(400,"비밀번호가 일치하지 않습니다."),
    NOT_FOUND_LOGINID(404, "존재하지 않는 로그인 아이디입니다."),
    DUPLICATE_DISEASEDATE_ERROR(404, "이미 등록된 날짜의 질병 데이터가 존재합니다."),
    NOT_FOUND_RECIPE(404, "존재하지 않는 레시피입니다."),
    DUPLICATE_EMAIL(400, "이미 등록된 이메일입니다."),
    ERROR_CREATE_CODE(500, "인증코드 생성에 실패하였습니다."),
    INVALID_EMAIL(409, "이메일이 인증되지 않았습니다."),
    ALREADY_EMAIL(409, "이미 인증된 계정입니다."),
    DELETE_FOOD_DATA_FAILED(404, "음식 데이터 삭제에 실패하였습니다."),
    DELETE_HEALTH_DATA_FAILED(404, "음식 데이터 삭제에 실패하였습니다."),
    NOT_TOKEN(403, "존재하지않는 토큰입니다."),
    ENCRYPTION_ERROR(500, "암호화 코드 생성에 실패하였습니다."),
    DECRYPTION_ERROR(500, "암호화 코드 해석에 실패하였습니다."),
    POST_UPLOAD_FAILED(500, "게시글 업로드 중 문제가 발생하였습니다."),
    AWS_S3_UPLOAD_FAILED(500, "이미지 업로드 중 문제가 발생하였습니다.");



    private final int status;
    private final String detail;

}

