$(document).ready(function() {
    var apiUrl = $('#apiUrl').data('url');

    $('#addIngredient').click(function() {
        addIngredientToList(''); // 사용자가 새 원재료를 수동으로 추가할 수 있게 함
    });

    $('#menuPostForm').submit(function(e) {
        e.preventDefault();
        var menuData = {
            date : $("#postDate").val(),
            mealType : $("#foodType").val(),
            mealTime : $("#foodTime").val(),
            foodName : $("#foodName").val(),
            ingredients : [],
            notes : $("#foodNotes").val()
        };
        $('#ingredientsList .ingredient-container').each(function() {
            var ingredientName = $(this).find('input').val();
            if (ingredientName) { // 입력값이 있는 경우에만 추가
                menuData.ingredients.push(ingredientName);
            }
        });
        e.preventDefault();
        $.ajax({
            url: apiUrl + '/menu_health_data/menu',
            type: "POST",
            headers: {
                'Authorization': sessionStorage.getItem("loginToken"),
            },
            contentType: 'application/json',
            data: JSON.stringify(menuData),
            success: function() {
                alert("식단을 성공적으로 추가하였습니다.");
                window.location.href = '/menu_health_data/select_date';
            },
            error: function() {
                alert("식단 추가에 실패하였습니다.");
                console.error('서버 요청 실패');
            }
        });

    });
});

