
$(document).ready(function() {
    $('#loginBtn').click(function(e) {
        var apiUrl = $('#apiUrl').data('url');
        var loginId = $('#loginId').val();
        var loginPassword = $('#loginPassword').val();

        var data = {
            loginId: loginId,
            loginPassword: loginPassword
        };
        e.preventDefault();
        $.ajax({
            url: apiUrl + '/login',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function(response) {
                saveLoginIdToSessionStorage(response);
                Swal.fire( // 성공 알림
                    'Done!',
                    '로그인이 완료되었습니다.',
                    'success'
                ).then((result) => {
                    if (result.value) {
                        window.location.href = 'main_menu/select';
                    }
                });
            },
            error: function(jqXHR) {
                if (jqXHR.status === 400) {
                    Swal.fire(
                        'Error!',
                        '없는 사용자이거나 비밀번호가 일치하지 않습니다',
                        'error'
                    );
                } else if (jqXHR.status === 409) {
                    let response = jqXHR.responseJSON;
                    let userPk = response.userPk;
                    if(userPk !== null) {
                        Swal.fire(
                            'Error!',
                            '이메일이 인증되지 않았습니다.',
                            'error'
                        ).then((result) => {
                            if (result.value) {
                                window.location.href = '/verify_email?userPk=' + userPk;
                            }
                        });
                    } else {
                        console.log("User PK not found in the response.");
                    }
                }
            }
        });

    });
});


function saveLoginIdToSessionStorage(response) {
    sessionStorage.setItem('accessToken', response.accessToken);
    sessionStorage.setItem('refreshToken', response.refreshToken);

}
