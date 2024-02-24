$(document).ready(function() {
    $('#addIngredient').click(function() {
        var $ingredientsList = $('#ingredientsList');

        // 원재료 입력 필드 생성
        var $ingredientField = $('<input>', {
            type: 'text',
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
            $(this).parent().remove();
        });

        // 원재료 입력 필드와 삭제 버튼을 포함하는 컨테이너 생성
        var $ingredientContainer = $('<div>', {class: 'ingredient-container'}).append($ingredientField, $deleteButton);

        // 전체 리스트에 추가
        $ingredientsList.append($ingredientContainer);
    });
});

function sendMenuData() {
    var menuData = {
        foodType : $("#foodType").val(),
        foodTime : $("#foodTime").val(),
        foodName : $("#foodName").val(),
        ingredients : [],
        notes : $("#notes").val()
    };

    $('#ingredientsList .ingredient-container').each(function() {
        var ingredientName = $(this).find('input').val();
        if (ingredientName) { // 입력값이 있는 경우에만 추가
            menuData.ingredients.push(ingredientName);
        }
    });

    console.log(menuData);
    $.ajax({
        url: '/menu-and-health-data/menuData', // 원하는 URL로 변경
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(menuData),
        success: function(response) {
            console.log('서버 응답:', response);

        },
        error: function(xhr, status, error) {
            console.error('에러:', error);

        }
    });

    return false;

}