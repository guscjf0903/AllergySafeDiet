$(document).ready(function() {
    var apiUrl = $('#apiUrl').data('url');
    var userPk = $('#userPk').data('user');


    // 이메일 인증 버튼 클릭 이벤트
    $('#verifyEmailBtn').click(function(e) {
        e.preventDefault();
        $('#verificationStatus').text(null);
        var email = $("#email").val();
        var data = {
            email: email
        };
        // API 호출로 이메일 데이터 전송
        $.ajax({
            url: apiUrl + '/email/verification_request', // API 엔드포인트를 여기에 입력하세요.
            type: 'POST',
            contentType: "application/json",
            data: JSON.stringify(data),

            success: function(response) {
                $('#verificationCodeSection').show();
                // 인증 코드 입력란을 표시
                Swal.fire( // 성공 알림
                    'Sent!',
                    'Verification email sent. Please check your email.',
                    'success'
                );
            },
            error: function(jqXHR) { // `jqXHR` 객체를 통해 에러 정보 접근
                let errorMessage = "Email verification failed. Please try again."; // 기본 에러 메시지
                if (jqXHR.status === 400) {
                    errorMessage = "이미 등록된 이메일입니다.";
                }
                $('#verificationStatus').text(errorMessage).css('color', 'red');
            }
        });
    });

    // 인증 코드 제출 버튼 클릭 이벤트
    $('#submitVerificationCodeBtn').click(function(e) {
        e.preventDefault(); // 페이지 리로드를 막기 위해 기본 동작 방지
        $('#verificationStatus').text(null);
        var email = $('#email').val();
        var verificationCode = $('#verificationCode').val();
        const data = {
            email: email,
            userPk: userPk,
            verificationCode: verificationCode
        };

        // API 호출로 이메일 및 인증 코드 데이터 전송
        $.ajax({
            url: apiUrl + '/emails/verifications',
            type: 'POST',
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function(response) {
                Swal.fire( // 성공 알림
                    'Done!',
                    'Email verify successful.',
                    'success'
                ).then((result) => {
                    if (result.value) {
                        window.location.href = '/login';
                    }
                });
            },
            error: function(jqXHR) { // `jqXHR` 객체를 통해 에러 정보 접근
                let errorMessage = "Email verification failed. Please try again."; // 기본 에러 메시지
                if (jqXHR.status === 409) {
                    Swal.fire( // 성공 알림
                        'Error!',
                        '이미 인증된 계정입니다..',
                        'error'
                    ).then((result) => {
                        if (result.value) {
                            window.location.href = '/login';
                        }
                    });
                }
                $('#verificationStatus').text(errorMessage).css('color', 'red');
            }
        });
    });

    // 인증 이메일 재발송 버튼 클릭 이벤트
    $('#resendVerificationEmailBtn').click(function() {
        $('#verifyEmailBtn').click(); // 이메일 인증 버튼의 클릭 이벤트를 재호출하여 처리
    });
});
