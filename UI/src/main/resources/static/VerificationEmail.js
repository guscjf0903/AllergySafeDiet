$(document).ready(function() {
    // 이메일 인증 버튼 클릭 이벤트
    $('#verifyEmailBtn').click(function() {
        var email = $('#email').val();
        var data = {
            email: email
        };
        // API 호출로 이메일 데이터 전송
        $.ajax({
            url: '/email/verification_request', // API 엔드포인트를 여기에 입력하세요.
            type: 'POST',
            contentType: "application/json",
            data: JSON.stringify(data),

            success: function(response) {
                $('#verificationCodeSection').show();
                // 인증 코드 입력란을 표시
                alert("Verification email sent. Please check your email.");
            },
            error: function() {
                alert("Failed to send verification email. Please try again.");
            }
        });
    });

    // 인증 코드 제출 버튼 클릭 이벤트
    $('#submitVerificationCodeBtn').click(function() {
        var email = $('#email').val();
        var verificationCode = $('#verificationCode').val();
        // API 호출로 이메일 및 인증 코드 데이터 전송
        $.ajax({
            url: 'YOUR_VERIFICATION_API_ENDPOINT', // 인증 API 엔드포인트를 여기에 입력하세요.
            type: 'POST',
            data: {
                email: email,
                verificationCode: verificationCode
            },
            success: function(response) {
                $('#verificationEmail')
                $('#verificationStatus').text("Email verification successful!");
            },
            error: function() {
                $('#verificationStatus').text("Email verification failed. Please try again.");
            }
        });
    });

    // 인증 이메일 재발송 버튼 클릭 이벤트
    $('#resendVerificationEmailBtn').click(function() {
        $('#verifyEmailBtn').click(); // 이메일 인증 버튼의 클릭 이벤트를 재호출하여 처리
    });
});
