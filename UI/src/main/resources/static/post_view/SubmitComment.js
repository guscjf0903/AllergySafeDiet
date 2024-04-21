$('#submitComment').click(function() {
    const apiUrl = $('#apiUrl').data('url');
    const postId = new URLSearchParams(window.location.search).get('post_id');

    let commentText = $('#commentText').val();
    if (!commentText) {
        alert('댓글을 입력해주세요.');
        return;
    }
    sendAuthenticatedRequest({
        url: apiUrl + "/comments/create",
        method: 'POST',
        data: {
            postId: postId,
            commentText: commentText
        },
        onSuccess: function() {
            fetchComments(postId);
            $('#commentText').val('');
        },
        onError: function(jqXHR) {
            alert('댓글 작성에 실패했습니다. 다시 시도해주세요.');
            console.log(jqXHR);
        }
    });
});
