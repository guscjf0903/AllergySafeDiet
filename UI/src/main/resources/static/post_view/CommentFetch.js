function fetchComments(postId) {
    const apiUrl = $('#apiUrl').data('url');
    $.ajax({
        url: apiUrl + "/comments?postId=" + postId,
        method: 'GET',
        headers: {
            'Authorization': sessionStorage.getItem("loginToken"),
        },
        success: function (comments) {
            displayComments(comments);
        },
        error: function () {
            console.error("댓글을 불러오는 중 에러가 발생했습니다.");
        }
    });
}

function displayComments(comments) {
    let commentsHtml = comments.map(comment => {
        let authorTag = comment.isAuthor ? " (작성자)" : ""; //댓글이 작성자가 적은 댓글인가? 확인

        let repliesHtml = comment.replies?.map(reply => `
            <div class="card mt-2">
                <div class="card-body">
                    <b>${reply.replyAuthor}${reply.isAuthor ? " (작성자)" : ""}</b> - ${reply.replyText}
                </div>
            </div>
        `).join('') || ''; // 대댓글 확인, replies가 null이면 빈 문자열을 사용
        console.log(repliesHtml);
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
                    ${repliesHtml}
                    ${replyInputHtml}
                </div>
            </div>
        `;
    }).join('');

    $('#commentsList').html(commentsHtml);
    attachReplyHandlers();

}
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

        $.ajax({
            url: apiUrl + "/comments/reply",
            method: 'POST',
            headers: {
                'Authorization': sessionStorage.getItem("loginToken"),
            },
            contentType: 'application/json',
            data: JSON.stringify({
                commentId: commentId,
                replyText: replyText
            }),
            success: function() {
                fetchComments(postId); // 댓글과 대댓글을 다시 로드
            },
            error: function(jqXHR) {
                if (jqXHR.status === 401) {
                    Swal.fire('Error!', '로그인이 필요합니다.', 'error')
                        .then((result) => {
                            if (result.value) {
                                window.location.href = '/login';
                            }
                        });
                } else {
                    alert('대댓글 작성에 실패했습니다.');
                }
            }
        });
    });
}