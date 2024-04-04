const apiUrl = $('#apiUrl').data('url');
let selectedData = {
    food: [],
    health: []
};

$(document).ready(function () {
    $('#addDate').click(function () {
        const selectedDate = $('#datePicker').val();
        if (!selectedDate) {
            alert('날짜를 선택해주세요.');
            return;
        }
        fetchDataForDate(selectedDate);
    });

    $('#submitPost').click(function () {
        const postData = {
            title: $('#postTitle').val(),
            content: $('#postContent').val(),
            foodIds: selectedData.food.map(food => food.id),
            healthId: selectedData.health.map(health => health.id),
        };

        $.ajax({
            url: apiUrl + "/post/upload",
            method: 'POST',
            headers: {
                'Authorization': sessionStorage.getItem("loginToken"),
            },
            contentType: 'application/json',
            data: JSON.stringify(postData),
            success: function(data) {
                console.log("title: " + postData.title + "content: " + postData.content +
                    "foodIds: " + postData.foodIds + "healthIds: " + postData.healthId);
                alert("게시물을 업로드 하였습니다.");
                window.location.href = '/post/list';
            },
            error: function(jqXHR) {
                handleAjaxError(jqXHR);
            }
        });
        console.log("제목:", title, "내용:", content, "선택된 데이터 Ids:", dataIds);
    });
});

function fetchDataForDate(date) {
    fetchDataType(date, 'food');
    fetchDataType(date, 'health');
}

function fetchDataType(date, type) {
    let urlType = type === 'food' ? '/food_health_data/food?date=' : '/food_health_data/health?date=';
    $.ajax({
        url: apiUrl + urlType + date,
        method: 'GET',
        headers: {
            'Authorization': sessionStorage.getItem("loginToken"),
        },
        success: function(data) {
            if ((Array.isArray(data) && data.length === 0) || data === undefined) {
                alert(`해당 날짜에 ${type} 데이터가 없습니다.`);
                return;
            }
            displayData(type, data, date);
            if (type === 'food') {
                selectedData.food.push(...data.map(food => ({id: food.id, date: date})));
            } else {
                selectedData.health.push({id: data.id, date: date});
            }
        },
        error: function(jqXHR) {
            handleAjaxError(jqXHR);
        }
    });
}

function displayData(type, data, date) {
    let containerId = `date-container-${date.replace(/-/g, '')}`; // 날짜별 컨테이너 ID 생성
    let $dateContainer = $("#" + containerId);

    if ($dateContainer.length === 0) {
        // 날짜별 컨테이너가 없으면 생성
        $dateContainer = $(`<div class="date-container" id="${containerId}">
                                <h4>${date}</h4>
                                <div class="health-data"></div>
                                <div class="food-data row"></div>
                            </div>`);
        $('#dataLists').append($dateContainer);
    }

    let detailsHtml = '';
    if (type === 'food') {
        data.forEach(food => {
            const ingredientsList = food.ingredients.map(ingredient => ingredient.ingredientName).join(', ');
            detailsHtml += `<div class="col-md-4 data-item food-item" data-id="${food.id}">
                                <p>식사 종류: ${food.mealType}<br>
                                식사 시간: ${food.mealTime}<br>
                                음식 이름: ${food.foodName}<br>
                                원재료: ${ingredientsList}<br>
                                특이사항: ${food.foodNotes}</p>
                                <button class="btn btn-danger delete-data" data-type="food" data-id="${food.id}">삭제</button>
                            </div>`;
        });
        $dateContainer.find('.food-data').append(detailsHtml);
    } else {
        let pillsContent = data.pills && data.pills.length > 0 ? data.pills.map(pill => `${pill.name} - ${pill.count}개`).join(', ') : '알약 정보 없음';
        detailsHtml = `<div class="data-item health-item" data-id="${data.id}">
                            <p>알러지 수치: ${data.allergiesStatus}<br>
                            컨디션 수치: ${data.conditionStatus}<br>
                            몸무게: ${data.weight}kg<br>
                            수면 시간: ${data.sleepTime}시간<br>
                            특이사항: ${data.healthNotes}<br>
                            알약 정보: ${pillsContent}</p>
                            <button class="btn btn-danger delete-data" data-type="health" data-id="${data.id}">삭제</button>
                        </div>`;
        $dateContainer.find('.health-data').append(detailsHtml);
    }
}
$(document).on('click', '.delete-data', function () {
    const $dataItem = $(this).closest('.data-item');
    const $dateContainer = $(this).closest('.date-container');

    const id = $(this).data('id');
    const type = $(this).data('type');

    $dataItem.remove();
    selectedData[type] = selectedData[type].filter(item => item.id !== id);

    const remainingItemsCount = $dateContainer.find('.data-item').length;

    if (remainingItemsCount === 0) {
        $dateContainer.remove();
    }
});

function handleAjaxError(jqXHR) {
    if (jqXHR.status === 401) {
        Swal.fire('Error!', '로그인이 되지 않았습니다.', 'error').then((result) => {
            if (result.value) {
                window.location.href = '/login';
            }
        });
    } else {
        console.error('An error occurred:', jqXHR.statusText);
    }
}
