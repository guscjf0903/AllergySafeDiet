function fetchComments(postId) {
    const apiUrl = $('#apiUrl').data('url');
    sendAuthenticatedRequest({
        url: apiUrl + "/comments?postId=" + postId,
        method: 'GET',
        data: {},
        onSuccess: function(comments) {
            displayComments(comments);
        },
        onError: function(jqXHR) {
            alert('게시물 조회에 실패했습니다.');
        }
    });
}

function displayComments(comments) {
    let commentsHtml = comments.map(comment => {
        let authorTag = comment.isPostCommentAuthor ? " (작성자)" : ""; //댓글이 작성자가 적은 댓글인가? 확인
        let deleteButtonHtml = comment.isOwnComment ? `<button class="btn btn-danger delete-comment" data-comment-id="${comment.id}">댓글 삭제</button>` : '';<!--게시물 조회자가 본인이 쓴 댓글인지 확인하고 삭제버튼 활성화-->


        let repliesHtml = comment.replies?.map(reply => {
            let replyDeleteButtonHtml = reply.isOwnReply ? `<button class="btn btn-danger delete-reply" data-reply-id="${reply.id}">대댓글 삭제</button>` : ''; <!--게시물 조회자가 본인이 쓴 대댓글인지 확인하고 삭제버튼 활성화-->
            return `            
            <div class="card mt-2">
                <div class="card-body">
                    <b>${reply.replyAuthor}${reply.isPostReplyAuthor ? " (작성자)" : ""}</b> - ${reply.replyText}
                    ${replyDeleteButtonHtml}
                </div>
            </div>
        `}).join('') || ''; // 대댓글 확인, replies가 null이면 빈 문자열을 사용

        // 대댓글 입력 필드 추가
        let replyInputHtml = `
            <div class="reply-input">
                <textarea class="form-control mt-2" placeholder="대댓글을 입력하세요..." rows="2"></textarea>
                <button class="btn btn-primary mt-2 submit-reply" data-comment-id="${comment.id}">대댓글 작성</button>
            </div>
        `;

        return `
            <div class="card mb-3" data-comment-id="${comment.id}">
                <div class="card-body">
                    <b>${comment.author}${authorTag}</b> - ${comment.text}  
                    ${deleteButtonHtml}
                    ${repliesHtml}
                    ${replyInputHtml}
                </div>
            </div>
        `;
    }).join('');

    $('#commentsList').html(commentsHtml);
    attachReplyHandlers();
}

