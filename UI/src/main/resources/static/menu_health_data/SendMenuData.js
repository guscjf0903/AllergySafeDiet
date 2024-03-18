$('#addIngredient').click(function() {
    addIngredientToList(''); // 사용자가 새 원재료를 수동으로 추가할 수 있게 함
});
function addIngredientToList(ingredientName) {
    var $ingredientsList = $('#ingredientsList');
    // 원재료 입력 필드 생성 (이미 존재하는 값이 있으면 그 값을 사용)
    var $ingredientField = $('<input>', {
        type: 'text',
        value: ingredientName, // API로부터 받은 재료명 또는 빈 문자열
        placeholder: '재료 이름',
        class: 'form-control',
        required: true
    });

    // 삭제 버튼 생성
    var $deleteButton = $('<button>', {
        text: '삭제',
        type: 'button',
        class: 'btn btn-danger btn-sm'
    }).click(function() {
        $(this).parent().remove(); // 해당 원재료 컨테이너 삭제
    });

    // 원재료 입력 필드와 삭제 버튼을 포함하는 컨테이너 생성
    var $ingredientContainer = $('<div>', {class: 'ingredient-container'}).append($ingredientField, $deleteButton);

    // 전체 리스트에 추가
    $ingredientsList.append($ingredientContainer);
}
$(document).ready(function() {
    $('#menuPostForm').submit(function(e) {
        var apiUrl = $('#apiUrl').data('url');
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
            type: 'POST',
            headers: {
                'Authorization': sessionStorage.getItem("loginToken"),
            },
            contentType: 'application/json',
            data: JSON.stringify(menuData),
            success: function() {
                alert("식단을 성공적으로 추가하였습니다.");
                window.location.href = '/menu_health_data/' + $("#postDate").val();
            },
            error: function() {
                alert("식단 추가에 실패하였습니다.");
                console.error('서버 요청 실패');
            }
        });

    });
});