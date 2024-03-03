function SendLogin() {
    var loginId = $('#loginId').val();
    var loginPassword = $('#loginPassword').val();

    var data = {
        loginId: loginId,
        loginPassword: loginPassword
    };

    $.ajax({
        url: '/login',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function(response) {
            console.log(response.loginToken);
            saveLoginIdToSessionStorage(response.loginToken);
            alert('로그인이 완료되었습니다.');
        },
        error: function(error) {
            console.error('request failed', error);
            alert('로그인에 실패하였습니다.');
        }
    });

    return false;
}

function saveLoginIdToSessionStorage(loginToken) {
    sessionStorage.setItem('loginToken', loginToken);
}
