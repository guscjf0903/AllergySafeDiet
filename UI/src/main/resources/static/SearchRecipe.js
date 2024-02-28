$(document).ready(function() {
    $('#searchRecipe').click(function() {
        var foodName = $('#foodName').val(); // 음식 이름 값 가져오기
        console.log(foodName)
        if(foodName) { // 음식 이름이 비어있지 않은 경우
            $.ajax({
                url: '/menu_health_data/recipes',
                type: 'GET', // HTTP 메소드
                data: { foodName: foodName }, // 요청과 함께 보낼 데이터
                success: function(data) {
                    console.log(data);
                    alert("레시피 검색 성공!");
                },
                error: function() {
                    alert("레시피 검색 실패. 다시 시도해주세요.");
                }
            });
        } else {
            alert("음식 이름을 입력해주세요.");
        }
    });
});
