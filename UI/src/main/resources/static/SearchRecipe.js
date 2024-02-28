$(document).ready(function() {
    $('#searchRecipe').click(function() {
        var foodName = $('#foodName').val(); // 음식 이름 값 가져오기
        if(foodName) { // 음식 이름이 비어있지 않은 경우
            $.ajax({
                url: '/menu_health_data/recipes',
                type: 'GET', // HTTP 메소드
                data: { foodName: foodName }, // 요청과 함께 보낼 데이터
                success: function(data) {
                    updateIngredientsList(data);
                    alert("원재료 검색 성공!");
                },
                error: function() {
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
    ingredients.forEach(function(ingredient) {
        addIngredientToList(ingredient.ingredientName); // 각 원재료를 리스트에 추가
    });
}