$(document).ready(function () {
    $('.menu-button').on('click', function () {
        window.location.href = '/food_health_data/select_date';
    });

    $('.health-analysis-button').on('click', function () {
        window.location.href = '/health-analysis';
    });

    $('.community-button').on('click', function () {
        window.location.href = '/post/list';
    });

    $('.allergy-info-button').on('click', function () {
        window.location.href = '/allergy_info';
    });

    var logoutButton = document.getElementById('logoutButton');

    logoutButton.addEventListener('click', function (e) {
        e.preventDefault();
        sessionStorage.removeItem('loginToken');
        Swal.fire( // 성공 알림
            'Logout!',
            'success logout.',
            'success'
        ).then((result) => {
            if (result.value) {
                window.location.href = '/login';
            }
        });
    });
});