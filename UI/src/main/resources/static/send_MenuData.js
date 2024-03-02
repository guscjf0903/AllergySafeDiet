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



//
// $(document).ready(function() {
//     $('#addIngredient').click(function() {
//         var $ingredientsList = $('#ingredientsList');
//
//         // 원재료 입력 필드 생성
//         var $ingredientField = $('<input>', {
//             type: 'text',
//             placeholder: '재료 이름',
//             class: 'form-control',
//             required: true
//         });
//
//         // 삭제 버튼 생성
//         var $deleteButton = $('<button>', {
//             text: '삭제',
//             type: 'button',
//             class: 'btn btn-danger btn-sm'
//         }).click(function() {
//             $(this).parent().remove();
//         });
//
//         // 원재료 입력 필드와 삭제 버튼을 포함하는 컨테이너 생성
//         var $ingredientContainer = $('<div>', {class: 'ingredient-container'}).append($ingredientField, $deleteButton);
//
//         // 전체 리스트에 추가
//         $ingredientsList.append($ingredientContainer);
//     });
// });

function sendMenuData() {
    var menuData = {
        loginToken : sessionStorage.getItem("loginToken"),
        date : $("#postDate").val(),
        mealType : $("#foodType").val(),
        mealTime : $("#foodTime").val(),
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

    $.ajax({
        url: '/menu_health_data/menu', // 원하는 URL로 변경
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(menuData),
        success: function(response) {
            alert("식단을 성공적으로 추가하였습니다.");
            window.location.href = '/menu-and-health-data/' + $("#postDate").val();
            console.log('서버 응답:', response);
        },
        error: function(xhr, status, error) {
            alert("식단 추가에 실패하였습니다.");
            var errorMessage = JSON.parse(xhr.responseText);
            console.error('에러:', errorMessage);
        }
    });

    return false;

}