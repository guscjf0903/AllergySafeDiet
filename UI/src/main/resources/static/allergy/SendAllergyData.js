let allergyDataExists = false; // 전역 변수 선언

$(document).ready(function() {
    fetchAllergies();

    // 항목 추가 버튼 이벤트
    $('#addButton').click(function() {
        addAllergyField();
    });

    // 삭제 버튼 이벤트
    $('body').on('click', '.remove-btn', function() {
        $(this).closest('.input-group').remove();
    });

    // 저장 버튼 이벤트
    $('#saveButton').click(function() {
        const apiUrl = $('#apiUrl').data('url');
        var allergyData = $('.allergy-info').map(function() { return $(this).val(); }).get();
        var data = {
            allergy: allergyData
        }
        const method = allergyDataExists ? 'PUT' : 'POST';
        sendAuthenticatedRequest({
            url: apiUrl + "/allergy",
            method: method,
            data: data,
            onSuccess: function(xhr) {
                alert("성공적으로 저장하였습니다.");
            },
            onError: function(jqXHR) {
                alert("저장하는 과정에 문제가 발생했습니다.");
                console.error(jqXHR);
            }
        });
    });
});
function fetchAllergies() {
    const apiUrl = $('#apiUrl').data('url');
    sendAuthenticatedRequest({
        url: `${apiUrl}/allergy`,
        method: 'GET',
        data: {},
        onSuccess: function(response) {
            if (response.allergies && response.allergies.length > 0) {
                response.allergies.forEach(allergy => addAllergyField(allergy));
                allergyDataExists = true;
            } else {
                addAllergyField();
                allergyDataExists = false;
            }
        },
        onError: function(jqXHR) {
            const message = jqXHR.status === 404 ? '데이터를 찾을 수 없습니다.' : '데이터를 불러오는데 문제가 발생했습니다.';
            $('#dataStatusMessage').text(message).show();
            console.error(jqXHR.responseText);
        }
    });
}

function addAllergyField(value = '') {
    var fieldHTML = `<div class="input-group mb-2">
            <input type="text" class="form-control allergy-info" placeholder="알레르기 정보 입력" value="${value}">
            <div class="input-group-append">
                <button class="btn btn-outline-danger remove-btn" type="button">삭제</button>
            </div>
        </div>`;
    $('#allergyFields').append(fieldHTML);
}