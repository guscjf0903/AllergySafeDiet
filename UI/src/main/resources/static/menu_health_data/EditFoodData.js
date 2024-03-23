let postDate;
$(document).ready(function() {
    const apiUrl = $('#apiUrl').data('url');
    checkMenuData(); // 메뉴 데이터 확인

    $('#addIngredient').click(function() {
        addIngredientToList(''); // 사용자가 새 원재료를 수동으로 추가할 수 있게 함
    });

    $('#menuPostForm').submit(function(e) {
        e.preventDefault();
        const foodData = {
            date: postDate,
            mealType: $("#foodType").val(),
            mealTime: $("#foodTime").val(),
            foodName: $("#foodName").val(),
            ingredients: [],
            notes: $("#foodNotes").val()
        };
        $('#ingredientsList .ingredient-container').each(function() {
            const ingredientName = $(this).find('input').val();
            if (ingredientName) { // 입력값이 있는 경우에만 추가
                foodData.ingredients.push(ingredientName);
            }
        });
        $.ajax({
            url: apiUrl + '/food_health_data/food?id=' +  $("#id").data('id'),
            type: "PUT",
            headers: {
                'Authorization': sessionStorage.getItem("loginToken"),
            },
            contentType: 'application/json',
            data: JSON.stringify(foodData),
            success: function() {
                alert("식단을 성공적으로 수정하였습니다.");
                window.location.href = '/food_health_data/select_date';
            },
            error: function() {
                alert("식단 수정에 실패하였습니다.");
            }
        });

    });
});

function checkMenuData() {
    const apiUrl = $('#apiUrl').data('url');

    $.ajax({
        url: apiUrl + '/food_health_data/food?id=' + $("#id").data('id'),
        method: 'GET',
        headers: {
            'Authorization': sessionStorage.getItem("loginToken"),
        },
        success: function(data) {
            $("#foodType").val(data.mealType);
            $("#foodTime").val(data.mealTime);
            $("#foodName").val(data.foodName);
            $("#foodNotes").val(data.foodNotes);
            postDate = data.date;
            // 기존에 추가된 재료 목록을 비우고 새로운 재료 목록을 할당.
            $('#ingredientsList').empty();
            data.ingredients.forEach(function(ingredient) {
                addIngredientToList(ingredient.ingredientName); // 이미 정의된 함수를 사용하여 재료 목록에 추가
            });
        },
        error: function() {
            alert("해당 식단 데이터가 없습니다.");
            window.location.href = '/food_health_data/select_date';
        }
    });
}