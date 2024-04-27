$(document).ready(function() {
    var apiUrl = $('#apiUrl').data('url');
    var userPk = $('#userPk').data('user');


    // 이메일 인증 버튼 클릭 이벤트
    $('#verifyEmailBtn').click(function(e) {
        e.preventDefault();
        $('#verificationStatus').text(null);
        var email = $("#email").val();

        sendAuthenticatedRequest({
            url: apiUrl + '/email/verification_request',
            method: 'POST',
            data: { email: email },
            onSuccess: function(response) {
                $('#verificationCodeSection').show();
                Swal.fire(
                    'Sent!',
                    'Verification email sent. Please check your email.',
                    'success'
                );
            },
            onError: function(jqXHR) {
                let errorMessage = jqXHR.status === 400 ? "이미 등록된 이메일입니다." : "Email verification failed. Please try again.";
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
        sendAuthenticatedRequest({
            url: apiUrl + '/emails/verifications',
            method: 'POST',
            data: {
                email: email,
                userPk: $('#userPk').data('user'),
                verificationCode: verificationCode
            },
            onSuccess: function(response) {
                Swal.fire(
                    'Done!',
                    'Email verify successful.',
                    'success'
                ).then((result) => {
                    if (result.value) {
                        window.location.href = 'main_menu/select';
                    }
                });
            },
            onError: function(jqXHR) {
                let errorMessage = "Email verification failed. Please try again."; // 기본 에러 메시지
                if (jqXHR.status === 409) {
                    Swal.fire( // 성공 알림
                        'Error!',
                        '이미 인증된 계정입니다.',
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
