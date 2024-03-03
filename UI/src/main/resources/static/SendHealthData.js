$(document).ready(function() {
    checkHealthData();

    $('#healthDataSubmit').submit(function(e) {
        var healthData = {
            loginToken : sessionStorage.getItem("loginToken"),
            date : $("#postDate").val(),
            allergiesStatus : $("#allergiesStatus").val(),
            conditionStatus : $("#conditionStatus").val(),
            weight : $("#weight").val(),
            sleepTime : $("#sleepTime").val(),
            healthNotes : $("#healthNotes").val()
        };
        e.preventDefault();
        $.ajax({
            url: '/menu_health_data/health', // 실제 API 엔드포인트
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(healthData),

            success: function() {
                alert('데이터가 성공적으로 저장되었습니다.');
                window.location.href = '/menu_health_data/' + $("#postDate").val();
            },
            error: function() {
                alert('데이터 저장에 실패했습니다. 다시 시도해주세요.');
                console.error('데이터 저장에 실패했습니다.');
            }
        });
        alert('Form submitted!');
    });

    function checkHealthData() {
        // API 호출하여 데이터 존재 여부 확인
        var postDate = $('#postDate').val(); // 날짜 입력 필드에서 날짜 가져오기
        $.ajax({
            url: '/menu_health_data/health', // 실제 API 엔드포인트
            method: 'GET',
            contentType: 'application/json',
            data: {
                date: postDate,
                loginToken: sessionStorage.getItem("loginToken")
            },
            complete: function(xhr) {
                if(xhr.status === 200) {
                    var response = JSON.parse(xhr.responseText);
                    $('#allergiesStatus').val(response.allergiesStatus);
                    $('#conditionStatus').val(response.conditionStatus);
                    $('#weight').val(response.weight);
                    $('#sleepTime').val(response.sleepTime);
                    $('#healthNotes').val(response.healthNotes);
                } else if(xhr.status === 204) {
                    $('#dataStatusMessage').text('해당 날짜에 데이터가 없습니다. 새로운 데이터를 입력해주세요.').show();
                    $('#allergiesStatus').val('');
                    $('#conditionStatus').val('');
                    $('#weight').val('');
                    $('#sleepTime').val('');
                    $('#healthNotes').val('');
                }
            },
            error: function() {
                $('#dataStatusMessage').text('데이터를 불러오는데 문제가 발생했습니다.');
                console.error('데이터를 불러오는데 문제가 발생했습니다.');
            }
        });
    }
});
