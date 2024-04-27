function attachReplyHandlers() {
    $('.submit-reply').click(function() {
        let commentId = $(this).data('comment-id');
        let replyText = $(this).prev('textarea').val();
        if (!replyText) {
            alert('대댓글을 입력해주세요.');
            return;
        }
        const apiUrl = $('#apiUrl').data('url');
        const postId = new URLSearchParams(window.location.search).get('post_id');

        sendAuthenticatedRequest({
            url: apiUrl + "/comments/reply",
            method: 'POST',
            data: {
                commentId: commentId,
                replyText: replyText
            },
            onSuccess: function() {
                fetchComments(postId); // 댓글과 대댓글을 다시 로드
            },
            onError: function(jqXHR) {
                alert('대댓글 작성에 실패했습니다.');
            }
        });

    });

    $('.delete-comment').click(function() {
        let commentId = $(this).data('comment-id');
        if (confirm('이 댓글을 삭제하시겠습니까?')) {
            const apiUrl = $('#apiUrl').data('url');
            sendAuthenticatedRequest({
                url: apiUrl + "/comments/" + commentId,
                method: 'DELETE',
                onSuccess: function() {
                    alert('댓글이 삭제되었습니다.');
                    window.location.reload();  // 페이지 새로고침
                },
                onError: function(jqXHR) {
                    alert('댓글 삭제에 실패했습니다.');
                }
            });
        }
    });

    $('.delete-reply').click(function() {
        let replyId = $(this).data('reply-id');
        console.log("replyId:", replyId);  // 현재 replyId의 값 확인

        if (confirm('이 대댓글을 삭제하시겠습니까?')) {
            const apiUrl = $('#apiUrl').data('url');
            sendAuthenticatedRequest({
                url: apiUrl + "/comments/reply/" + replyId,
                method: 'DELETE',
                onSuccess: function() {
                    alert('대댓글이 삭제되었습니다.');
                    window.location.reload();  // 페이지 새로고침

                },
                onError: function(jqXHR) {
                    alert('댓글 삭제에 실패했습니다.');
                }
            });
        }
    });
}