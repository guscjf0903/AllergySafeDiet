function sendAuthenticatedRequest({url, method, data, onSuccess, onError}) {
    const ajaxOptions = {
        url: url,
        type: method,
        headers: {
            'Authorization': 'Bearer ' + sessionStorage.getItem("accessToken"),
        },
        contentType: 'application/json',
        success: function(response) {
            onSuccess(response);
        },
        error: function(jqXHR) {
            if (jqXHR.status === 401) {  // 토큰 만료 감지
                refreshAccessToken(() => {
                    sendAuthenticatedRequest({url, method, data, onSuccess, onError});  // 토큰 갱신 후 요청 재시도
                }, onError);
            } else {
                onError(jqXHR);
            }
        }
    };

    // 데이터가 있고, 요청 메서드가 'GET'이 아닐 때만 데이터 추가
    if (data && Object.keys(data).length > 0 && method.toUpperCase() !== 'GET') {
        ajaxOptions.data = JSON.stringify(data);
    }

    // AJAX 요청 실행
    $.ajax(ajaxOptions);
}

function refreshAccessToken(onSuccess, onError) {
    $.ajax({
        url: '/path/to/refresh',  // 리프레시 토큰 엔드포인트
        method: 'POST',
        headers: {
            'Refresh-Token': sessionStorage.getItem("refreshToken")
        },
        error: function(jqXHR) {
            if (jqXHR.status === 401 || jqXHR.status === 403) {  // 리프레시 토큰 만료 또는 무효
                Swal.fire({
                    title: 'Session Expired!',
                    text: 'Please log in again to continue.',
                    icon: 'warning',
                    confirmButtonText: 'Login'
                }).then((result) => {
                    if (result.value) {
                        sessionStorage.clear();  // 세션 스토리지 클리어
                        window.location.href = '/login';  // 로그인 페이지로 이동
                    }
                });
            } else {
                onError(jqXHR);
            }
        },
        success: function(response) {
            sessionStorage.setItem('accessToken', response.accessToken);  // 새 액세스 토큰 저장
            onSuccess();
        }
    });
}
