$('#submitComment').click(function() {
    const apiUrl = $('#apiUrl').data('url');
    const postId = new URLSearchParams(window.location.search).get('post_id');

    let commentText = $('#commentText').val();
    if (!commentText) {
        alert('댓글을 입력해주세요.');
        return;
    }

    $.ajax({
        url: apiUrl + "/comments/create",
        method: 'POST',
        headers: {
            'Authorization': sessionStorage.getItem("loginToken"),
        },
        contentType: 'application/json',
        data: JSON.stringify({
            postId: postId,
            commentText: commentText
        }),
        success: function() {
            fetchComments(postId);
            $('#commentText').val(''); // 입력 필드를 비우기
        },
        error: function(jqXHR) {
            if (jqXHR.status === 401) {
                Swal.fire(
                    'Error!',
                    '로그인이 되지 않았습니다.',
                    'error'
                ).then((result) => {
                    if (result.value) {
                        window.location.href = '/login';
                    }
                });
            }
        }
    });
});
