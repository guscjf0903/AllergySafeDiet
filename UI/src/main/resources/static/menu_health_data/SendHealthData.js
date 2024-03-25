let healthDataExists = false; // 전역 변수 선언

$(document).ready(function () {
    checkHealthData();
    const apiUrl = $('#apiUrl').data('url');

    $('#addPill').click(function () {
        addPillToList('', ''); // 사용자가 새 알약 정보를 수동으로 추가할 수 있게 함
    });

    $('#healthPostForm').submit(function (e) {
        const healthData = {
            date: $("#postDate").val(),
            allergiesStatus: $("#allergiesStatus").val(),
            conditionStatus: $("#conditionStatus").val(),
            weight: $("#weight").val(),
            sleepTime: $("#sleepTime").val(),
            healthNotes: $("#healthNotes").val(),
            pills: []
        };
        console.log(healthData);

        $('#pillList .pill-container').each(function () {
            const pillName = $(this).find('input[type=text]').val();
            const pillCount = $(this).find('input[type=number]').val();
            if (pillName && pillCount) {
                healthData.pills.push({name: pillName, count: parseInt(pillCount)});
            }
        });

        const method = healthDataExists ? 'PUT' : 'POST';
        e.preventDefault();
        $.ajax({
            url: apiUrl + '/food_health_data/health', // 실제 API 엔드포인트
            method: method,
            headers: {
                'Authorization': sessionStorage.getItem("loginToken"),
            },
            contentType: 'application/json',
            data: JSON.stringify(healthData),

            success: function () {
                alert('데이터가 성공적으로 저장되었습니다.');
                window.location.href = '/food_health_data/select_date';
            },
            error: function () {
                alert('데이터 저장에 실패했습니다. 다시 시도해주세요.');
                console.error('데이터 저장에 실패했습니다.');
            }
        });
    });
});


function addPillToList(pillName, pillCount) {
    const $pillList = $('#pillList');
    // 알약 이름 입력 필드 생성
    const $pillNameField = $('<input>', {
        type: 'text',
        value: pillName,
        placeholder: '알약 이름',
        class: 'form-control col mr-2',
        required: true
    });
    // 알약 개수 입력 필드 생성
    const $pillCountField = $('<input>', {
        type: 'number',
        value: pillCount,
        placeholder: '개수',
        class: 'form-control col',
        required: true
    });
    // 삭제 버튼 생성
    const $deleteButton = $('<button>', {
        text: '삭제',
        type: 'button',
        class: 'btn btn-danger btn-sm ml-2'
    }).click(function () {
        $(this).parent().remove(); // 해당 알약 정보 컨테이너 삭제
    });
    // 알약 정보 컨테이너 생성
    const $pillContainer = $('<div>', {class: 'pill-container d-flex align-items-center mt-2'}).append($pillNameField, $pillCountField, $deleteButton);
    // 전체 리스트에 추가
    $pillList.append($pillContainer);
}


function checkHealthData() {
    // API 호출하여 데이터 존재 여부 확인
    const apiUrl = $('#apiUrl').data('url');

    const postDate = $('#postDate').val(); // 날짜 입력 필드에서 날짜 가져오기
    $.ajax({
        url: apiUrl + '/food_health_data/health', // 실제 API 엔드포인트
        method: 'GET',
        headers: {
            'Authorization': sessionStorage.getItem("loginToken"),
        },
        contentType: 'application/json',
        data: {
            date: postDate,
        },
        complete: function (xhr) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                $('#allergiesStatus').val(response.allergiesStatus);
                $('#conditionStatus').val(response.conditionStatus);
                $('#weight').val(response.weight);
                $('#sleepTime').val(response.sleepTime);
                $('#healthNotes').val(response.healthNotes);
                response.pills.forEach(function (pill) {
                    addPillToList(pill.name, pill.count);
                });
                healthDataExists = true;
            } else if (xhr.status === 204) {
                $('#dataStatusMessage').text('해당 날짜에 데이터가 없습니다. 새로운 데이터를 입력해주세요.').show();
                $('#allergiesStatus, #conditionStatus, #weight, #sleepTime, #healthNotes').val('');
                healthDataExists = false;
            }
        },
        error: function (response) {
            $('#dataStatusMessage').text('데이터를 불러오는데 문제가 발생했습니다.');
            console.error(response);
        }
    });
}
