// $(document).ready(function () {
//     var postId = new URLSearchParams(window.location.search).get('post_id');
//     if (postId) {
//         fetchPostDetailsForEdit(postId);  // 게시글 상세 정보를 불러오는 함수
//     }
//
//     function fetchPostDetailsForEdit(postId) {
//         var apiUrl = $('#apiUrl').data('url');
//         sendAuthenticatedRequest({
//             url: apiUrl + "/post/detail?postId=" + postId,
//             method: 'GET',
//             onSuccess: function(data) {
//                 // 데이터를 HTML 입력 필드에 채워 넣기
//                 $('#postTitle').val(data.title);
//                 $('#postContent').val(data.content);
//                 // 이미지와 기타 데이터 처리
//                 if (data.images) {
//                     data.images.forEach(function(imageUrl) {
//                         var imageElement = $('<img>').attr('src', imageUrl).addClass('existing-image');
//                         $('#imagePreviewContainer').append(imageElement);
//                     });
//                 }
//             },
//             onError: function(jqXHR) {
//                 console.error("게시물 정보를 불러오는 중 에러 발생: ", jqXHR);
//             }
//         });
// });