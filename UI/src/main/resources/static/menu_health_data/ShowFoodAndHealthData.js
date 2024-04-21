const apiUrl = $('#apiUrl').data('url');

$(document).ready(function() {
    $('#datePicker').change(function() {
        const selectedDate = encodeURIComponent($(this).val());  // 날짜 인코딩
        fetchFoodData(selectedDate);
        fetchHealthData(selectedDate);
    });

    $('#addMenuBtn').click(function() {
        window.location.href = '/food_health_data/food?date=' + $("#datePicker").val();
    });

    $('#editHealthBtn').click(function() {
        window.location.href = '/food_health_data/health?date=' + $("#datePicker").val();
    });
});

function fetchFoodData(date) {

    sendAuthenticatedRequest({
        url: apiUrl + `/food_health_data/food?date=${date}`,
        method: 'GET',
        data: {},
        onSuccess: function(response) {
            displayFoodData(response);
        },
        onError: function(jqXHR) {
            $('#foodData').html('<p>음식관련 데이터가 없습니다. 추가해주세요.</p>');
        }
    });
}

function fetchHealthData(date) {
    sendAuthenticatedRequest({
        url: apiUrl + `/food_health_data/health?date=${date}`,
        method: 'GET',
        data: {},
        onSuccess: function(data) {
            displayHealthData(data);
        },
        onError: function(jqXHR) {
            $('#healthData').html('<p>건강관련 데이터가 없습니다. 추가해주세요.</p>');
            $('#editHealthBtn').text('건강 데이터 추가');
        }
    });
}

function displayFoodData(data) {
    if (!Array.isArray(data) || data.length === 0) {
        $('#foodData').html('<p>음식관련 데이터가 없습니다. 추가해주세요.</p>');
        return;
    }
    data.sort((a, b) => a.mealTime.localeCompare(b.mealTime));

    let htmlContent = '<div class="table-responsive"><table class="table"><thead><tr><th>날짜</th><th>식사 종류</th><th>식사 시간</th><th>음식 이름</th><th>원재료</th><th>특이사항</th><th>조치</th></tr></thead><tbody>';

    data.forEach(food => {
        const ingredientsList = food.ingredients.map(ingredient => ingredient.ingredientName);
        htmlContent += `<tr>
            <td>${food.date}</td>
            <td>${food.mealType}</td>
        수소            <td>${food.foodName}</td>
            <td>${ingredientsList.join(', ')}</td>
            <td>${food.foodNotes}</td>
            <td>
                <button class="btn btn-primary btn-sm edit-menu" data-id="${food.id}">수정</button>
                <button class="btn btn-danger btn-sm delete-menu" data-id="${food.id}">삭제</button>
            </td>
        </tr>`;
    });

    htmlContent += '</tbody></table></div>';

    $('#foodData').html(htmlContent);

    $('.edit-menu').click(function() {
        window.location.href = '/food_health_data/food/edit?id=' + $(this).data('id');
    });
    $('.delete-menu').click(function() {
        const menuId = $(this).data('id')
        deleteFoodData(menuId);
    });
}

function displayHealthData(data) {
    if (!data) {
        $('#healthData').html('<p>건강관련 데이터가 없습니다. 추가해주세요.</p>');
        $('#editHealthBtn').text('건강 데이터 추가');
        return;
    }

    let pillsContent = '';
    if (data.pills && data.pills.length > 0) {
        pillsContent += data.pills.map(pill => `${pill.name} - ${pill.count}개`).join(', ');
    } else {
        pillsContent = '알약 정보 없음';
    }

    let htmlContent = `
        <div데이터 (${data.date})</h3>
            <p><strong>알러지 수치:</strong> ${data.allergiesStatus}</p>
            <p><strong>컨디션 수치:</strong> ${data.conditionStatus}</p>
            <p><strong>몸무게:</strong> ${data.weight} kg</p>
            <p><strong>수면 시간:</strong> ${data.sleepTime} 시간</p>
            <p><strong>건강 노트:</strong> ${data.healthNotes}</p>
            <p><strong>알약 정보:</strong> ${pillsContent}</p>
        </div>`;

    $('#healthData').html(htmlContent);
    $('#editHealthBtn').text('건강 데이터 수정'); // 데이터가 존재하므로 버튼 텍스트를 '수정'으로 변경
}

function deleteFoodData(menuId) {
    sendAuthenticatedRequest({
        url: apiUrl + '/food_health_data/food?id=' + menuId,
        method: "DELETE",
        data: {},
        onSuccess: function() {
            alert("식단을 성공적으로 삭제하였습니다.");
            window.location.href = '/food_health_data/select_date';
        },
        onError: function(jqXHR) {
            alert("식단 삭제에 실패하였습니다.");
            console.error('서버 요청 실패');
        }
    });
}
