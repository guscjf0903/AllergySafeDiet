$(document).ready(function () {
    var apiUrl = $('#apiUrl').attr('data-url'); // API URL 동적 참조
    fetchPosts(1, 10);

    // 게시물 클릭 이벤트 핸들러
    $('#postList').on('click', 'tr', function () {
        var postId = $(this).data('id');
        fetchPostDetails(postId);
    });

    // 게시물 작성 페이지 이동
    $('#createPost').click(function () {
        window.location.href = '/post/upload';
    });
});

function fetchPosts(page, limit) {
    sendAuthenticatedRequest({
        url: apiUrl + '/post/list?page=' + page + '&limit=' + limit,
        method: 'GET',
        onSuccess: function (data) {
            displayPosts(data.posts);
            setupPagination(data.total, limit, page);
        },
        onError: function (jqXHR) {
            console.error('Error fetching posts:', jqXHR);
        }
    });
}

function displayPosts(posts) {
    var postList = $('#postList');
    postList.empty(); // 이전 목록을 초기화합니다.
    posts.forEach(function (post) {
        postList.append(
            `<tr data-id="${post.id}">
                <td>${post.id}</td>
                <td>${post.date}</td>
                <td>${post.author}</td>
                <td>${post.title}</td>
                <td>${post.views}</td>
            </tr>`
        );
    });
}
function setupPagination(totalItems, itemsPerPage, currentPage) {
    var totalPages = Math.ceil(totalItems / itemsPerPage);
    var pagination = $('.pagination');
    pagination.empty();

    for (let i = 1; i <= totalPages; i++) {
        var pageItem = $(`<li class="page-item ${i === currentPage ? 'active' : ''}"><a class="page-link" href="#">${i}</a></li>`);
        pageItem.on('click', function (e) {
            e.preventDefault();
            fetchPosts(i, itemsPerPage);
        });
        pagination.append(pageItem);
    }
}

function fetchPostDetails(postId) {
    $.ajax({
        url: `https://your-api-endpoint.com/posts/${postId}`, // 실제 API 엔드포인트로 대체해야 합니다.
        method: 'GET',
        success: function (data) {
            //displayPostDetails(data);
        },
        error: function (err) {
            console.error(`Error fetching post details for post ${postId}:`, err);
        }
    });
}

// function displayPostDetails(post) {
//     // 게시물 상세 정보를 표시하는 코드...
//     // 예: 모달 팝업, 새 페이지, 페이지 내 세부 섹션 등
// }