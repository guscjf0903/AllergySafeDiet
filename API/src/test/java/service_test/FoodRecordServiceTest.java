//package service_test;
//
//import java.time.LocalDateTime;
//import java.util.Date;
//import org.api.entity.FoodEntity;
//import org.api.entity.LoginEntity;
//import org.api.entity.UserEntity;
//import org.api.repository.FoodRepository;
//import org.api.service.FoodRecordService;
//import org.api.service.IngredientService;
//import org.api.service.LoginService;
//import org.core.request.FoodRequest;
//import org.core.response.FoodResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.junit.jupiter.api.extension.ExtendWith;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.Optional;
//
//@ExtendWith(MockitoExtension.class)
//public class FoodRecordServiceTest {
//
//    @Mock
//    private LoginService loginService;
//
//    @Mock
//    private FoodRepository foodRepository;
//
//    @Mock
//    private IngredientService ingredientService;
//
//    @InjectMocks
//    private FoodRecordService foodRecordService;
//
//    private UserEntity testUser;
//    private LoginEntity testLogin;
//
//    private FoodRequest foodRequest;
//    private FoodEntity testFoodEntity;
//
//    @BeforeEach
//    void setUp() {
//        // UserEntity와 LoginEntity 초기화
//        testUser = new UserEntity("testUser", "tnthtn", "test@example.com", new Date(), "male", 180);
//        testUser.setUserId(1L); // testUser에 유효한 사용자 ID 설정
//
//        testLogin = new LoginEntity(testUser, "auth-header-value", LocalDateTime.now().plusHours(1));
//
//        // FoodRequest 및 FoodEntity 예제 생성
//        foodRequest = new FoodRequest(LocalDate.now(), "저녁", LocalTime.of(8, 0), "오믈렛",
//                List.of("계란", "우유", "치즈"), "굳굳!");
//        testFoodEntity = FoodEntity.of(testUser, foodRequest);
//        testFoodEntity.setFoodRecordId(1L); // foodRecordId을 설정
//
//
//        // Mock 설정
//        lenient().when(loginService.validateLoginId(anyString())).thenReturn(testLogin);
//        lenient().when(foodRepository.save(any(FoodEntity.class))).thenReturn(testFoodEntity);
//        lenient().when(foodRepository.findById(anyLong())).thenReturn(Optional.of(testFoodEntity));
//        lenient().when(foodRepository.getFoodDataByFoodRecordIdAndUserUserId(1L, 1L)).thenReturn(Optional.of(testFoodEntity));
//    }
//
//    @Test
//    void 음식데이터_저장테스트() {
//        // 실행
//        FoodEntity result = foodRecordService.saveFoodData(foodRequest, "auth-header-value");
//
//        // 로그인 서비스를 통해 유저 인증이 일어났는지 확인
//        verify(loginService).validateLoginId("auth-header-value");
//        // FoodEntity가 저장되었는지 확인
//        verify(foodRepository).save(any(FoodEntity.class));
//        // 반환된 FoodEntity가 예상대로 설정되었는지 확인
//        assertNotNull(result);
//        assertEquals("오믈렛", result.getFoodName());
//    }
//
//    @Test
//    void id_음식데이터_테스트() {
//        // 실행
//        Optional<FoodResponse> result = foodRecordService.getFoodDataById(1L, "auth-header-value");
//
//        // 음식 데이터 조회가 올바르게 이루어졌는지 확인
//        verify(foodRepository).getFoodDataByFoodRecordIdAndUserUserId(1L, 1L);
//        // 반환된 Optional<FoodResponse>가 비어 있지 않고, 예상한 값과 일치하는지 확인
//        assertTrue(result.isPresent());
//        assertEquals("오믈렛", result.get().foodName());
//    }
//
//    @Test
//    void 음식데이터_업데이트_테스트() {
//        // 미리 정의된 testFoodEntity를 사용하여 mock 설정
//        when(foodRepository.getFoodDataByFoodRecordIdAndUserUserId(anyLong(), anyLong())).thenReturn(Optional.of(testFoodEntity));
//
//        // 실행
//        foodRecordService.putFoodData(1L, foodRequest, "auth-header-value");
//
//        // 검증: 업데이트 로직이 호출되었는지 확인
//        verify(foodRepository).getFoodDataByFoodRecordIdAndUserUserId(anyLong(), anyLong());
//        verify(ingredientService).putIngredientData(any(FoodEntity.class), eq(foodRequest));
//    }
//
//
//}
