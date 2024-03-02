$(document).ready(function() {
    checkHealthData();

    $('#addDataButton').click(function() {
        $('#healthPostForm').show();
        $(this).hide();
    });

    $('#editDataButton').click(function() {
        $('#healthPostForm').show();
        $(this).hide();
    });

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
                window.location.href = '/menu-and-health-data/' + $("#postDate").val();
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
            data: {date: postDate},
            success: function(response) {
                if (response.dataExists) {
                    $('#dataStatusMessage').text('해당 날짜에 데이터가 존재합니다. 수정하시겠습니까?');
                    $('#editDataButton').show();

                    $('#allergiesStatus').val(response.data.allergiesStatus);
                    $('#conditionStatus').val(response.data.conditionStatus);
                    $('#weight').val(response.data.weight);
                    $('#sleepTime').val(response.data.sleepTime);
                    $('#healthNotes').val(response.data.healthNotes);

                } else {
                    $('#dataStatusMessage').text('해당 날짜에 데이터가 없습니다. 추가하시겠습니까?');
                    $('#addDataButton').show();
                }
            },
            error: function() {
                $('#dataStatusMessage').text('데이터를 불러오는데 문제가 발생했습니다.');
            }
        });
    }
});
