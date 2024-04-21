$(document).ready(function () {
    const apiUrl = $('#apiUrl').data('url');

    $('#addIngredient').click(function () {
        addIngredientToList(''); // 사용자가 새 원재료를 수동으로 추가할 수 있게 함
    });

    $('#menuPostForm').submit(function (e) {
        e.preventDefault();
        const foodData = {
            date: $("#postDate").val(),
            mealType: $("#foodType").val(),
            mealTime: $("#foodTime").val(),
            foodName: $("#foodName").val(),
            ingredients: [],
            notes: $("#foodNotes").val()
        };
        $('#ingredientsList .ingredient-container').each(function () {
            let ingredientName = $(this).find('input').val();
            if (ingredientName) { // 입력값이 있는 경우에만 추가
                foodData.ingredients.push(ingredientName);
            }
        });
        sendAuthenticatedRequest({
            url: apiUrl + '/food_health_data/food',
            method: "POST",
            data: foodData,
            onSuccess: function () {
                alert("식단을 성공적으로 수정하였습니다.");
                window.location.href = '/food_health_data/select_date';
            },
            onError: function (jqXHR) {
                alert("식단 수정에 실패하였습니다.");
                console.error("Error: " + jqXHR.responseText);
            }
        });
    });
});

