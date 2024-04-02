const apiUrl = $('#apiUrl').data('url');
let selectedData = {
    food: [],
    health: []
};
$(document).ready(function() {
    $('#addDate').click(function() {
        const selectedDate = $('#datePicker').val();
        if (!selectedDate) {
            alert('날짜를 선택해주세요.');
            return;
        }
        fetchDataForDate(selectedDate);
    });

    $('#submitPost').click(function() {
        const title = $('#postTitle').val();
        const content = $('#postContent').val();
        const dataIds = {
            foodIds: selectedData.food.map(food => food.id),
            healthId: selectedData.health.map(health => health.id)
        };

        console.log("제목:", title, "내용:", content, "선택된 데이터 Ids:", dataIds);
    });
});

function fetchDataForDate(date) {
    $.ajax({
        url: apiUrl + `/food_health_data/food?date=${date}`,
        method: 'GET',
        headers: {
            'Authorization': sessionStorage.getItem("loginToken"),
        },
        success: function(data) {
            if (!Array.isArray(data) || data.length === 0) {
                alert("해당 날짜에 데이터가 없습니다.");
                return;
            }
            data.forEach(food => {
                displayData('food', food, date);
                selectedData.food.push({ id: food.id, date: date });
            });
        },
        error: function(err) {
            console.error('Error fetching food data:', err);
        }
    });

    // 건강 데이터 조회
    // $.ajax({
    //     url: `https://your-api-endpoint.com/health?date=${date}`,
    //     method: 'GET',
    //     headers: {
    //         'Authorization': sessionStorage.getItem("loginToken"),
    //     },
    //     success: function(data) {
    //         if (data) {
    //             displayData('health', data, date);
    //             selectedData.health.push({ id: data.id, date: date });
    //         }
    //     },
    //     error: function(err) {
    //         console.error('Error fetching health data:', err);
    //     }
    // });
}

function displayData(type, data, date) {
    let detailsHtml = '';

    if (type === 'food') {
        const ingredientsList = data.ingredients.map(ingredient => ingredient.ingredientName);
        detailsHtml = `
            <h5>${date} 식단 데이터</h5>
            <p>식사 종류: ${data.mealType}</p>
            <p>식사 시간: ${data.mealTime}</p>
            <p>음식 이름: ${data.foodName}</p>
            <p>원재료: ${ingredientsList.join(', ')}</p>
            <p>특이사항: ${data.foodNotes}</p>
        `;
    } else if (type === 'health') {
        // 건강 데이터의 상세 정보를 HTML로 구성
        const pillsHtml = Object.entries(data.pills)
            .map(([pillName, count]) => `${pillName} - ${count}개`).join(', ');

        detailsHtml = `
            <h5>${date} 건강 데이터</h5>
            <p>알러지 수치: ${data.allergyScore}</p>
            <p>컨디션 수치: ${data.conditionScore}</p>
            <p>몸무게: ${data.weight}kg</p>
            <p>수면 시간: ${data.sleepHours}시간</p>
            <p>특이사항: ${data.specialNote}</p>
            <p>알약 정보: ${pillsHtml}</p>
        `;
    }

    const dataHtml = `
        <div class="col-md-4 mb-3">
            <div class="data-item" id="${type}-${data.id}">
                ${detailsHtml}
                <button class="btn btn-danger delete-data" data-type="${type}" data-id="${data.id}">삭제</button>
            </div>
        </div>
    `;
    let currentRow = $('#dataLists .row').last();
    if (currentRow.length === 0 || currentRow.children('.col-md-4').length >= 3) {
        // 현재 row가 없거나 이미 3개의 항목을 포함하고 있으면 새로운 row를 생성
        currentRow = $('<div class="row"></div>').appendTo('#dataLists');
    }
    currentRow.append(dataHtml);

    $(`.delete-data[data-id="${data.id}"]`).click(function() {
        const id = $(this).data('id');
        const dataType = $(this).data('type');
        $(`#${dataType}-${id}`).parent('.col-md-4').remove();
        selectedData[dataType] = selectedData[dataType].filter(item => item.id !== id);
    });
}
