$(document).ready(function () {
    var postId = getPostIdFromUrl();
    fetchPostDetails(postId);
    fetchComments(postId);
});

function getPostIdFromUrl() {
    return new URLSearchParams(window.location.search).get('post_id');
}

function fetchPostDetails(postId) {
    var apiUrl = $('#apiUrl').data('url');
    $.ajax({
        url: apiUrl + "/post/detail?postId=" + postId,
        method: 'GET',
        headers: {
            'Authorization': sessionStorage.getItem("loginToken"),
        },
        xhrFields: {
            withCredentials: true // 클라이언트와 서버가 통신할때 쿠키 값을 공유하겠다는 설정
        },
        success: function (data) {
            displayPostDetails(data);
        },
        error: function (error) {
            Swal.fire(
                'Error!',
                '게시물이 존재하지 않습니다.',
                'error'
            );
            console.error("게시물을 불러오는 중 에러가 발생했습니다.", error);
        }
    });
}

