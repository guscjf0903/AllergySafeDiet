package service_test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import org.api.entity.UserEntity;
import org.api.exception.CustomException;
import org.api.exception.ErrorCodes;
import org.api.repository.UserRepository;
import org.api.service.SignupService;
import org.core.request.SignupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class SignupTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private SignupService signupService;

    private SignupRequest validSignupRequest;
    private SignupRequest invalidEmailSignupRequest;
    private SignupRequest invalidPasswordSignupRequest;


    // 각 테스트 실행 전에 실행되어 필요한 초기 설정을 수행
    @BeforeEach
    public void setUp() {
         validSignupRequest = new SignupRequest("testUser1", "tnthtn35!", "test@example.com", new Date(), "male", 180, true);
         invalidEmailSignupRequest = new SignupRequest("testUser2", "tnthtn35!", "test@example.com", new Date(), "female", 180, false);
         invalidPasswordSignupRequest = new SignupRequest("testUser3", "tnthtn35", "test@example.com", new Date(), "male", 180, false);
    }

    // 유효한 데이터로 사용자 등록이 성공하는 경우를 테스트합니다.
    @Test
    @DisplayName("save 저장 테스트")
    public void testRegisterUserWithValidData() {
        // SignupService의 registerUser 메소드를 유효한 데이터와 함께 호출하며,
        // 이 과정에서 예외가 발생하지 않는 것을 확인.
        assertDoesNotThrow(() -> signupService.registerUser(validSignupRequest));

        // UserRepository의 save 메소드가 정확히 한 번 호출되었는지 검증.
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    // 인증 이메일 확인이 실패했을 때 CustomException이 발생하는지 테스트합니다.
    @Test
    @DisplayName("메일 검증 테스트")
    public void testRegisterUserWithInvalidEmail() {
        // SignupService의 registerUser 메소드를 유효하지 않은 데이터와 함께 호출하며,
        // CustomException이 발생하는지 확인.
        CustomException exception = assertThrows(CustomException.class, () -> {
            signupService.registerUser(invalidEmailSignupRequest);
        });

        // 발생한 예외의 메시지가 예상한 값(INVALID_EMAIL)과 일치하는지 검증합니다.
        assertEquals(ErrorCodes.INVALID_EMAIL, exception.getErrorCode());
    }

    @Test
    @DisplayName("비밀번호 양식 테스트")
    public void testRegisterUserWithInvalidPassword() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        assertThrows(CustomException.class, () -> {
            signupService.registerUser(invalidPasswordSignupRequest);
        });
    }

}
