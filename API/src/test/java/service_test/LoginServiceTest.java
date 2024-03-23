package service_test;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.api.entity.LoginEntity;
import org.api.entity.UserEntity;
import org.api.exception.CustomException;
import org.api.repository.LoginRepository;
import org.api.repository.UserRepository;
import org.api.service.LoginService;
import org.core.request.LoginRequest;
import org.core.response.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private LoginRepository loginRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginService loginService;

    private UserEntity mockUser;
    private LoginRequest mockLoginRequest;
    private LoginEntity mockLoginEntity;

    @BeforeEach
    void setUp() {
        // 테스트에 사용될 모의 객체를 설정합니다.
        mockUser = new UserEntity(1L, "testUser", "password123", "user@test.com", new Date(), "male", 180, Instant.now().getEpochSecond());
        mockLoginRequest = new LoginRequest("testUser", "password123"); // LoginDto 클래스가 있다고 가정합니다.
        mockLoginEntity = new LoginEntity(1L, mockUser, UUID.randomUUID().toString(), LocalDateTime.now().plusHours(1), Instant.now().toEpochMilli());
    }

    @Test
    void 로그인성공_테스트() {
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(mockUser));
        when(loginRepository.save(any(LoginEntity.class))).thenReturn(mockLoginEntity);

        LoginResponse response = loginService.loginUser(mockLoginRequest);

        assertNotNull(response);
        assertNotNull(response.getLoginToken());
    }

    @Test
    void 유저가없을때_로그인_예외테스트() {
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.empty());

        // 로그인 시도 및 예외 검증
        assertThrows(CustomException.class, () -> loginService.loginUser(mockLoginRequest));
    }
    @Test
    void 로그인토큰_확인테스트() {
        when(loginRepository.findByLoginToken(anyString())).thenReturn(Optional.of(mockLoginEntity));

        // 토큰 검증 시도
        LoginEntity loginEntity = loginService.validateLoginId("validToken");

        // 검증
        assertNotNull(loginEntity);
    }

    @Test
    void 로그인토큰_예외테스트() {
        // loginRepository가 토큰을 찾지 못했을 때의 동작을 모의로 설정
        when(loginRepository.findByLoginToken(anyString())).thenReturn(Optional.empty());

        // 토큰 검증 시도 및 예외 검증
        assertThrows(CustomException.class, () -> loginService.validateLoginId("invalidToken"));
    }
}
