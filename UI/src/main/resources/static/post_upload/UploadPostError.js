function handleAjaxError(jqXHR) {
    if (jqXHR.status === 401) {
        Swal.fire('Error!', '로그인이 되지 않았습니다.', 'error').then((result) => {
            if (result.value) {
                window.location.href = '/login';
            }
        });
    } else {
        console.error('An error occurred:', jqXHR.statusText);
    }
}