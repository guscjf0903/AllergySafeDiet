$(document).ready(function() {
    var apiUrl = $('#apiUrl').data('url');

    // 이메일 인증 버튼 클릭 이벤트
    $('#verifyEmailBtn').click(function(e) {
        e.preventDefault(); // 페이지 리로드를 막기 위해 기본 동작 방지
        var email = $('#email').val();
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
            error: function() {
                Swal.fire( // 오류 알림
                    'Error!',
                    'Failed to send verification email. Please try again.',
                    'error'
                );
            }
        });
    });

    // 인증 코드 제출 버튼 클릭 이벤트
    $('#submitVerificationCodeBtn').click(function(e) {
        e.preventDefault(); // 페이지 리로드를 막기 위해 기본 동작 방지

        var email = $('#email').val();
        var verificationCode = $('#verificationCode').val();
        // API 호출로 이메일 및 인증 코드 데이터 전송
        $.ajax({
            url: apiUrl + '/emails/verifications',
            type: 'GET',
            data: {
                email: email,
                verificationCode: verificationCode
            },
            success: function(response) {
                $('#checkVerificationEmail').val('true');
                $('#verificationStatus').text("Email verification successful!").css('color', 'green');
            },
            error: function() {
                $('#checkVerificationEmail').val('false');
                $('#verificationStatus').text("Email verification failed. Please try again.").css('color', 'red');
            }
        });
    });

    // 인증 이메일 재발송 버튼 클릭 이벤트
    $('#resendVerificationEmailBtn').click(function() {
        $('#verifyEmailBtn').click(); // 이메일 인증 버튼의 클릭 이벤트를 재호출하여 처리
    });
});
