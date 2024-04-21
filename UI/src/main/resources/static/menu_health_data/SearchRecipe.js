$(document).ready(function () {
    const apiUrl = $('#apiUrl').data('url');

    $('#searchRecipe').click(function () {
        const foodName = $('#foodName').val();
        if (foodName) { // 음식 이름이 비어있지 않은 경우
            sendAuthenticatedRequest({
                url: apiUrl + '/recipes?foodName='+foodName,
                method: 'GET',
                onSuccess: function(data) {
                    updateIngredientsList(data);
                    alert("원재료 검색 성공!");
                },
                onError: function(jqXHR) {
                    alert("원재료 검색 실패. 다른 메뉴를 넣어주세요");
                }
            });
        } else {
            alert("음식 이름을 입력해주세요.");
        }
    });
});


function updateIngredientsList(ingredients) {
    $('#ingredientsList').empty(); // 리스트 초기화
    ingredients.forEach(function (ingredient) {
        addIngredientToList(ingredient.ingredientName); // 각 원재료를 리스트에 추가
    });
}