$(document).ready(function(){
    var postId = getPostIdFromUrl();
    fetchPostDetails(postId);
});

function getPostIdFromUrl() {
    return new URLSearchParams(window.location.search).get('post_id');
}

function fetchPostDetails(postId) {
    $.ajax({
        url: apiUrl + "?postId=" + postId,
        method: 'GET',
        headers: {
            'Authorization': sessionStorage.getItem("loginToken"),
        },
        success: function(data) {
            displayPostDetails(data);
        },
        error: function(error) {
            Swal.fire(
                'Error!',
                '게시물이 존재하지 않습니다.',
                'error'
            );
            console.error("게시물을 불러오는 중 에러가 발생했습니다.", error);
        }
    });
}

function displayPostDetails(data) {
    // 제목과 내용 채우기
    $('#postTitle').text(data.title);
    $('#postContent').text(data.content);

    // 이미지 채우기
    if (data.images && data.images.length > 0) {
        let carouselInner = $('<div class="carousel-inner"></div>');
        data.images.forEach((image, index) => {
            let itemClass = (index === 0) ? 'carousel-item active' : 'carousel-item';
            let carouselItem = $(`
                <div class="${itemClass}">
                    <img src="${image.url}" class="d-block w-100" alt="...">
                </div>
            `);
            carouselInner.append(carouselItem);
        });
        $('#postImages').append(carouselInner);
        $('#postImages').append(`
            <a class="carousel-control-prev" href="#postImages" role="button" data-slide="prev">
                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                <span class="sr-only">Previous</span>
            </a>
            <a class="carousel-control-next" href="#postImages" role="button" data-slide="next">
                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                <span class="sr-only">Next</span>
            </a>
        `);
    }

    // 건강 데이터 테이블 채우기
    if (data.healthData && data.healthData.length > 0) {
        let healthTableRows = data.healthData.map(health => `
            <tr>
                <td>${health.date}</td>
                <td>${health.weight}kg</td>
                <td>${health.sleepTime}시간</td>
                <td>${health.healthNotes}</td>
            </tr>
        `).join('');
        $('#healthData').html(`
            <thead>
                <tr>
                    <th>날짜</th>
                    <th>체중</th>
                    <th>수면 시간</th>
                    <th>메모</th>
                </tr>
            </thead>
            <tbody>
                ${healthTableRows}
            </tbody>
        `);
    }

    // 음식 데이터 테이블 채우기
    if (data.foodData && data.foodData.length > 0) {
        let foodTableRows = data.foodData.map(food => `
            <tr>
                <td>${food.date}</td>
                <td>${food.mealType}</td>
                <td>${food.foodName}</td>
            </tr>
        `).join('');
        $('#foodData').html(`
            <thead>
                <tr>
                    <th>날짜</th>
                    <th>식사 유형</th>
                    <th>음식명</th>
                </tr>
            </thead>
            <tbody>
                ${foodTableRows}
            </tbody>
        `);
    }
}
