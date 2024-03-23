
function addIngredientToList(ingredientName) {
    const $ingredientsList = $('#ingredientsList');
    // 원재료 입력 필드 생성 (이미 존재하는 값이 있으면 그 값을 사용)
    const $ingredientField = $('<input>', {
        type: 'text',
        value: ingredientName, // API로부터 받은 재료명 또는 빈 문자열
        placeholder: '재료 이름',
        class: 'form-control',
        required: true
    });

    // 삭제 버튼 생성
    const $deleteButton = $('<button>', {
        text: '삭제',
        type: 'button',
        class: 'btn btn-danger btn-sm'
    }).click(function () {
        $(this).parent().remove(); // 해당 원재료 컨테이너 삭제
    });

    // 원재료 입력 필드와 삭제 버튼을 포함하는 컨테이너 생성
    const $ingredientContainer = $('<div>', {class: 'ingredient-container'}).append($ingredientField, $deleteButton);

    // 전체 리스트에 추가
    $ingredientsList.append($ingredientContainer);
}