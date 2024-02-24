$(document).ready(function() {
    // 음식 이름 입력 필드에 데이터 입력 시 자동으로 연관 메뉴 제안
    $('#food-name').on('input', function() {
        var foodName = $(this).val();
        if (foodName.length > 2) { // 사용자가 2글자 이상 입력한 경우에만 요청
            $.ajax({
                url: 'menu-and-health-data/menu_suggestions', // API 엔드포인트 가정
                data: { menuName: foodName },
                success: function(data) {
                    $('#suggestions').empty();
                    data.forEach(function(menu) {
                        $('#suggestions').append(`<div class="suggestion-item" data-name="${menu.name}">${menu.name}</div>`);
                    });
                }
            });
        } else {
            $('#suggestions').empty();
        }
    });

    // 연관 메뉴 선택 시 바로 원재료 정보 로드
    $('#suggestions').on('click', '.suggestion-item', function() {
        var menuName = $(this).data('name');
        $('#food-name').val(menuName); // 입력 필드에 선택한 메뉴 이름을 설정
        $('#selected-menu-name').val(menuName); // 숨겨진 필드에 메뉴 이름을 저장
        $('#suggestions').empty(); // 제안 목록을 비웁니다.

        // 해당 메뉴의 원재료 정보 가져오기
        $.ajax({
            url: `API_ENDPOINT/menu_ingredients_by_name/${menuName}`,
            success: function(data) {
                $('#ingredients-list').empty(); // 기존 목록을 비웁니다.
                data.ingredients.forEach(function(ingredient) {
                    $('#ingredients-list').append(`<div class="ingredient-item">${ingredient} <span class="delete-ingredient">[삭제]</span></div>`);
                });
                $('#add-ingredient').show(); // 원재료 추가 버튼을 보여줌
            }
        });
    });

    // 원재료 삭제 기능
    $('#ingredients-list').on('click', '.delete-ingredient', function() {
        $(this).parent().remove(); // 해당 원재료를 목록에서 삭제합니다.
    });

    // 추가: 원재료 추가 기능
    $('#add-ingredient').on('click', function() {
        var ingredientName = prompt("추가할 원재료 이름을 입력하세요:");
        if(ingredientName) {
            $('#ingredients-list').append(`<div class="ingredient-item">${ingredientName} <span class="delete-ingredient">[삭제]</span></div>`);
        }
    });
});
