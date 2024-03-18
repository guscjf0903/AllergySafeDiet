$(document).ready(function() {
    $('#datePicker').change(function() {
        const selectedDate = $(this).val();
        fetchFoodData(selectedDate);
        fetchHealthData(selectedDate);
    });

    $('#addMenuBtn').click(function() {
        // 메뉴 추가 로직
    });

    $('#editHealthBtn').click(function() {
        // 건강 데이터 수정 로직
    });
});

function fetchFoodData(date) {
    // 예시 URL 및 AJAX 요청, 실제 URL로 교체 필요
    $.ajax({
        url: `https://api.example.com/foodData?date=${date}`,
        type: 'GET',
        success: function(data) {
            displayFoodData(data);
        },
        error: function() {
            $('#foodData').html('<p>음식관련 데이터가 없습니다. 추가해주세요.</p>');
        }
    });
}

function fetchHealthData(date) {
    // 예시 URL 및 AJAX 요청, 실제 URL로 교체 필요
    $.ajax({
        url: `https://api.example.com/healthData?date=${date}`,
        type: 'GET',
        success: function(data) {
            displayHealthData(data);
        },
        error: function() {
            $('#healthData').html('<p>건강관련 데이터가 없습니다. 추가해주세요.</p>');
            $('#editHealthBtn').text('건강 데이터 추가');
        }
    });
}

function displayFoodData(data) {
    if (!data || data.length === 0) {
        $('#foodData').html('<p>음식관련 데이터가 없습니다. 추가해주세요.</p>');
        return;
    }

    let htmlContent = '<div class="table-responsive"><table class="table"><thead><tr><th>날짜</th><th>식사 종류</th><th>식사 시간</th><th>음식 이름</th><th>원재료</th><th>특이사항</th><th>조치</th></tr></thead><tbody>';

    data.forEach(menu => {
        htmlContent += `<tr>
            <td>${menu.date}</td>
            <td>${menu.mealType}</td>
            <td>${menu.mealTime}</td>
            <td>${menu.foodName}</td>
            <td>${menu.ingredients.join(', ')}</td>
            <td>${menu.notes}</td>
            <td>
                <button class="btn btn-primary btn-sm edit-menu" data-id="${menu.id}">수정</button>
                <button class="btn btn-danger btn-sm delete-menu" data-id="${menu.id}">삭제</button>
            </td>
        </tr>`;
    });

    htmlContent += '</tbody></table></div>';

    $('#foodData').html(htmlContent);

    $('.edit-menu').click(function() {
        const menuId = $(this).data('id');
        // 메뉴 수정 로직 실행
    });
    $('.delete-menu').click(function() {
        const menuId = $(this).data('id');
        // 메뉴 삭제 로직 실행
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
        pillsContent += data.pills.map(pill => `${pill.name} - ${pill.quantity}개`).join(', ');
    } else {
        pillsContent = '알약 정보 없음';
    }

    let htmlContent = `
        <div>
            <h3>건강 데이터 (${data.date})</h3>
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
