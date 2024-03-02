function sendSignupData() {
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
    $.ajax({
        url: "/signup",
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
    })
    return false;
}