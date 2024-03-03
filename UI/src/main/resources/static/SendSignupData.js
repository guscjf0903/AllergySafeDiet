$(document).ready(function() {
    $('#signupBtn').click(function(e) {
        var apiUrl = $('#apiUrl').data('url');

        var userName = $("#userName").val();
        var password = $("#password").val();
        var email = $("#email").val();
        var birthDate = $("#birthDate").val();
        var gender = $('input[name="gender"]:checked').val();
        var height = $("#height").val();
        var checkVerificationEmail = $("#checkVerificationEmail").val();

        var data = {
            userName: userName,
            password: password,
            email: email,
            birthDate: birthDate,
            gender: gender,
            height: height,
            checkVerificationEmail: checkVerificationEmail
        };
        e.preventDefault();
        $.ajax({
            url: apiUrl + "/signup",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function (response) {
                console.log(response);
                Swal.fire( // 성공 알림
                    'Done!',
                    'Sign up successful.',
                    'success'
                );
                $('.tab a[href="#login"]').click();
            },
            error: function (error) {
                Swal.fire( // 오류 알림
                    'Error!',
                    'Failed to sign up. Please try again.',
                    'error'
                );
                console.log(error);
            }
        });
    });
});

$(document).ready(function() {
    $('.tab a').on('click', function (e) {
        e.preventDefault();

        $(this).parent().addClass('active');
        $(this).parent().siblings().removeClass('active');

        var target = $(this).attr('href');
        $('.tab-content > div').not(target).hide();

        $(target).fadeIn(600);
    });
});