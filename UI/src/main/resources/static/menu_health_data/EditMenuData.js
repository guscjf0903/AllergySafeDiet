var menuDataExists = false; // 전역 변수 선언

$(document).ready(function() {
    checkMenuData(); // 메뉴 데이터 확인
    var apiUrl = $('#apiUrl').data('url');

    $('#addIngredient').click(function() {
        addIngredientToList(''); // 사용자가 새 원재료를 수동으로 추가할 수 있게 함
    });

    $('#menuPostForm').submit(function(e) {
        e.preventDefault();
        const menuData = {
            date: $("#postDate").val(),
            mealType: $("#foodType").val(),
            mealTime: $("#foodTime").val(),
            foodName: $("#foodName").val(),
            ingredients: [],
            notes: $("#foodNotes").val()
        };
        $('#ingredientsList .ingredient-container').each(function() {
            var ingredientName = $(this).find('input').val();
            if (ingredientName) { // 입력값이 있는 경우에만 추가
                menuData.ingredients.push(ingredientName);
            }
        });
        $.ajax({
            url: apiUrl + '/menu_health_data/menu?id=' + $("#id").val(),
            type: "PUT",
            headers: {
                'Authorization': sessionStorage.getItem("loginToken"),
            },
            contentType: 'application/json',
            data: JSON.stringify(menuData),
            success: function() {
                alert("식단을 성공적으로 수정하였습니다.");
                window.location.href = '/menu_health_data/select_date';
            },
            error: function() {
                alert("식단 수정에 실패하였습니다.");
            }
        });

    });
});

function checkMenuData() {
    var apiUrl = $('#apiUrl').data('url');

    $.ajax({
        url: apiUrl + '/menu_health_data/menu?id=' + $("#id").data('id'),
        method: 'GET',
        headers: {
            'Authorization': sessionStorage.getItem("loginToken"),
        },
        success: function(data) {
            $("#foodType").val(data.mealType);
            $("#foodTime").val(data.mealTime);
            $("#foodName").val(data.foodName);
            $("#foodNotes").val(data.foodNotes);

            // 기존에 추가된 재료 목록을 비우고 새로운 재료 목록을 채웁니다.
            $('#ingredientsList').empty();
            data.ingredients.forEach(function(ingredient) {
                addIngredientToList(ingredient.ingredientName); // 이미 정의된 함수를 사용하여 재료 목록에 추가
            });
        },
        error: function() {
            alert("해당 식단 데이터가 없습니다.");
            window.location.href = '/menu_health_data/select_date';
        }
    });
}