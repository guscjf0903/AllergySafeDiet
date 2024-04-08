class FoodHealthDataFetcher {
    constructor(apiUrl) {
        this.apiUrl = apiUrl;
        this.selectedData = {
            food: [],
            health: []
        };
        $(document).on('click', '.delete-data', this.handleDeleteData.bind(this));
    }

    fetchDataForDate() {
        const selectedDate = $('#datePicker').val();
        if (!selectedDate) {
            alert('날짜를 선택해주세요.');
            return;
        }
        this.fetchDataType(selectedDate, 'food');
        this.fetchDataType(selectedDate, 'health');
    }

    fetchDataType(date, type) {
        let urlType = type === 'food' ? '/food_health_data/food?date=' : '/food_health_data/health?date=';
        $.ajax({
            url: this.apiUrl + urlType + date,
            method: 'GET',
            headers: {
                'Authorization': sessionStorage.getItem("loginToken"),
            },
            success: (data) => {
                if ((Array.isArray(data) && data.length === 0) || data === undefined) {
                    alert(`해당 날짜에 ${type} 데이터가 없습니다.`);
                    return;
                }
                this.displayData(type, data, date);
                if (type === 'food') {
                    this.selectedData.food.push(...data.map(food => ({id: food.id, date: date})));
                } else {
                    this.selectedData.health.push({id: data.id, date: date});
                }
            },
            error: (jqXHR) => {
                handleAjaxError(jqXHR);
            }
        });
    }

    displayData(type, data, date) {
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

    handleDeleteData(event) {
        const $dataItem = $(event.currentTarget).closest('.data-item');
        const $dateContainer = $(event.currentTarget).closest('.date-container');
        const id = $(event.currentTarget).data('id');
        const type = $(event.currentTarget).data('type');

        $dataItem.remove();
        this.selectedData[type] = this.selectedData[type].filter(item => item.id !== id);

        const remainingItemsCount = $dateContainer.find('.data-item').length;
        if (remainingItemsCount === 0) {
            $dateContainer.remove();
        }
    }


}

// $(document).on('click', '.delete-data', function () {
//     const $dataItem = $(this).closest('.data-item');
//     const $dateContainer = $(this).closest('.date-container');
//
//     const id = $(this).data('id');
//     const type = $(this).data('type');
//
//     $dataItem.remove();
//     this.selectedData[type] = this.selectedData[type].filter(item => item.id !== id);
//
//     const remainingItemsCount = $dateContainer.find('.data-item').length;
//
//     if (remainingItemsCount === 0) {
//         $dateContainer.remove();
//     }
// });