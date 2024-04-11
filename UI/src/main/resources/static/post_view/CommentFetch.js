function fetchComments(postId) {
    const apiUrl = $('#apiUrl').data('url');
    $.ajax({
        url: apiUrl + "/comments?postId=" + postId,
        method: 'GET',
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
        let repliesHtml = comment.replies.map(reply => ` 
            <div class="card mt-2" data-reply-id="${reply.id}">
                <div class="card-body">
                    <b>${reply.author}${reply.isAuthor ? " (작성자)" : ""}</b> - ${reply.text}
                </div>
            </div>
        `).join(''); //대댓글 확인.

        return `
            <div class="card mb-3">
                <div class="card-body">
                    <b>${comment.author}${authorTag}</b> - ${comment.text}
                    ${repliesHtml}
                </div>
            </div>
        `;
    }).join('');

    $('#commentsList').html(commentsHtml);
}
