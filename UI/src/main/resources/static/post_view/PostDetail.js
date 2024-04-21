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
    sendAuthenticatedRequest({
        url: apiUrl + "/post/detail?postId=" + postId,
        method: 'GET',
        data: {},
        onSuccess: function(data) {
            displayPostDetails(data);
        },
        onError: function(jqXHR) {
            console.error("게시물을 불러오는 중 에러가 발생했습니다.", jqXHR);
        }
    });
}

