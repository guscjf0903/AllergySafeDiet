function sendSignupData() {
    var userName = $("#userName").val();
    var password = $("#password").val();
    var email = $("#email").val();
    var birthDate = $("#birthDate").val();
    var gender = $('input[name="gender"]:checked').val();
    var height = $("#height").val();

    var data = {
        userName: userName,
        password: password,
        email: email,
        birthDate: birthDate,
        gender: gender,
        height: height,
    };
    $.ajax({
        url: "/signup",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (response) {
            console.log(response);
            alert("회원가입이 완료되었습니다.");
            $('.tab a[href="#login"]').click();
        },
        error: function (error) {
            alert("회원가입에 실패하였습니다.");
            console.log(error);
        }
    })
    return false;
}