$(document).ready(function() {
    $('#signupBtn').click(function(e) {
        var apiUrl = $('#apiUrl').data('url');

        var userName = $("#userName").val();
        var password = $("#password").val();
        var birthDate = $("#birthDate").val();
        var gender = $('input[name="gender"]:checked').val();
        var height = $("#height").val();

        var data = {
            userName: userName,
            password: password,
            birthDate: birthDate,
            gender: gender,
            height: height,
        };
        e.preventDefault();

        sendAuthenticatedRequest({
            url: apiUrl + "/signup",
            method: "POST",
            data: data,
            onSuccess: function(response) {
                Swal.fire(
                    'Done!',
                    'Sign up successful.',
                    'success'
                ).then((result) => {
                    if (result.value) {
                        window.location.href = 'verify_email?userPk=' + response.userPk;
                    }
                });
            },
            onError: function(jqXHR) {
                Swal.fire(
                    'Error!',
                    'Failed to sign up. Please try again.',
                    'error'
                );
                console.log(jqXHR.responseText);
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