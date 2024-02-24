function addIngredientField() {
    const ingredientsList = document.getElementById('ingredients-list');


    // Create new menu and quantity input fields
    const ingredientField = document.createElement('input');
    ingredientField.type = 'text';
    ingredientField.placeholder = '재료 이름';
    ingredientField.className = 'form-control';
    ingredientField.required = true;

    // 삭제 버튼 생성
    const deleteButton = document.createElement('button');
    deleteButton.textContent = '삭제';
    deleteButton.type = 'button';
    deleteButton.className = 'btn btn-danger btn-sm';
    deleteButton.onclick = function() {
        ingredientsList.removeChild(ingredientContainer);
    };

    // 원재료 입력 필드와 삭제 버튼을 포함하는 컨테이너 생성
    const ingredientContainer = document.createElement('div');
    ingredientContainer.className = 'ingredient-container';
    ingredientContainer.appendChild(ingredientField);
    ingredientContainer.appendChild(deleteButton);

    // 전체 리스트에 추가
    ingredientsList.appendChild(ingredientContainer);
}

