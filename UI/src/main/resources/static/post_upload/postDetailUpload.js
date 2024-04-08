// const apiUrl = $('#apiUrl').data('url');
// let selectedData = {
//     food: [],
//     health: []
// };
// let selectedFiles = [];
//
// $(document).ready(function () {
//     $('#addDate').click(function () {
//         const selectedDate = $('#datePicker').val();
//         if (!selectedDate) {
//             alert('날짜를 선택해주세요.');
//             return;
//         }
//         fetchDataForDate(selectedDate);
//     });
//
//     $('#postImages').change(function(event){
//         const files = event.target.files;
//         for (let i = 0; i < files.length; i++) {
//             const file = files[i];
//             selectedFiles.push(file);
//         }
//         createPreviews();
//     });
//
//     $('#imagePreviewContainer').on('click', '.btn-close', function(){
//         const index = parseInt($(this).data('index'), 10); // 삭제하려는 파일의 인덱스
//         selectedFiles.splice(index, 1); // 선택한 파일 목록에서 해당 파일 제거
//         createPreviews(); // 변경된 selectedFiles 기반으로 미리보기를 다시 생성
//     });
//
//     function createPreviews() {
//         $('#imagePreviewContainer').empty(); // 기존의 모든 미리보기를 먼저 제거
//         selectedFiles.forEach((file, index) => {
//             const reader = new FileReader();
//             reader.onload = function(e) {
//                 const previewHtml = `
//                 <div class="preview-wrapper me-2 position-relative" style="width: 35%; margin-top: 20px;">
//                     <img src="${e.target.result}" class="img-fluid" alt="Image Preview">
//                     <button type="button" class="btn-close position-absolute top-0 end-0" aria-label="Close" data-index="${index}"></button>
//                 </div>
//             `;
//                 $('#imagePreviewContainer').append(previewHtml);
//             };
//             reader.readAsDataURL(file);
//         });
//     }
//
//     $('#submitPost').click(function () {
//         $('#submitPost').prop('disabled', true); // 버튼 비활성화
//
//         const formData = new FormData();
//         formData.append('title', $('#postTitle').val());
//         formData.append('content', $('#postContent').val());
//         const foodIds = selectedData.food.map(food => food.id);
//         foodIds.forEach(id => {
//             formData.append('foodIds', id);
//         });
//         const healthIds = selectedData.health.map(health => health.id);
//         healthIds.forEach(id => {
//             formData.append('healthIds', id);
//         });
//
//         selectedFiles.forEach((file, index) => {
//             formData.append(`images[${index}]`, file);
//         });
//         console.log(selectedFiles.length);
//         const allImages = formData.getAll('images[0]');
//         const allImages2 = formData.getAll('images[1]');
//
//         console.log(allImages);
//         console.log(allImages2);
//
//         $.ajax({
//             url: apiUrl + "/post/upload",
//             method: 'POST',
//             headers: {
//                 'Authorization': sessionStorage.getItem("loginToken"),
//             },
//             processData: false,
//             contentType: false,
//             data: formData,
//             success: function() {
//                 alert("게시물을 업로드 하였습니다.");
//                 window.location.href = '/post/list';
//             },
//             error: function(jqXHR) {
//                 handleAjaxError(jqXHR);
//             }
//         });
//     });
// });
//
// function fetchDataForDate(date) {
//     fetchDataType(date, 'food');
//     fetchDataType(date, 'health');
// }
//
// function fetchDataType(date, type) {
//     let urlType = type === 'food' ? '/food_health_data/food?date=' : '/food_health_data/health?date=';
//     $.ajax({
//         url: apiUrl + urlType + date,
//         method: 'GET',
//         headers: {
//             'Authorization': sessionStorage.getItem("loginToken"),
//         },
//         success: function(data) {
//             if ((Array.isArray(data) && data.length === 0) || data === undefined) {
//                 alert(`해당 날짜에 ${type} 데이터가 없습니다.`);
//                 return;
//             }
//             displayData(type, data, date);
//             if (type === 'food') {
//                 selectedData.food.push(...data.map(food => ({id: food.id, date: date})));
//             } else {
//                 selectedData.health.push({id: data.id, date: date});
//             }
//         },
//         error: function(jqXHR) {
//             handleAjaxError(jqXHR);
//         }
//     });
// }
//
// function displayData(type, data, date) {
//     let containerId = `date-container-${date.replace(/-/g, '')}`; // 날짜별 컨테이너 ID 생성
//     let $dateContainer = $("#" + containerId);
//
//     if ($dateContainer.length === 0) {
//         // 날짜별 컨테이너가 없으면 생성
//         $dateContainer = $(`<div class="date-container" id="${containerId}">
//                                 <h4>${date}</h4>
//                                 <div class="health-data"></div>
//                                 <div class="food-data row"></div>
//                             </div>`);
//         $('#dataLists').append($dateContainer);
//     }
//
//     let detailsHtml = '';
//     if (type === 'food') {
//         data.forEach(food => {
//             const ingredientsList = food.ingredients.map(ingredient => ingredient.ingredientName).join(', ');
//             detailsHtml += `<div class="col-md-4 data-item food-item" data-id="${food.id}">
//                                 <p>식사 종류: ${food.mealType}<br>
//                                 식사 시간: ${food.mealTime}<br>
//                                 음식 이름: ${food.foodName}<br>
//                                 원재료: ${ingredientsList}<br>
//                                 특이사항: ${food.foodNotes}</p>
//                                 <button class="btn btn-danger delete-data" data-type="food" data-id="${food.id}">삭제</button>
//                             </div>`;
//         });
//         $dateContainer.find('.food-data').append(detailsHtml);
//     } else {
//         let pillsContent = data.pills && data.pills.length > 0 ? data.pills.map(pill => `${pill.name} - ${pill.count}개`).join(', ') : '알약 정보 없음';
//         detailsHtml = `<div class="data-item health-item" data-id="${data.id}">
//                             <p>알러지 수치: ${data.allergiesStatus}<br>
//                             컨디션 수치: ${data.conditionStatus}<br>
//                             몸무게: ${data.weight}kg<br>
//                             수면 시간: ${data.sleepTime}시간<br>
//                             특이사항: ${data.healthNotes}<br>
//                             알약 정보: ${pillsContent}</p>
//                             <button class="btn btn-danger delete-data" data-type="health" data-id="${data.id}">삭제</button>
//                         </div>`;
//         $dateContainer.find('.health-data').append(detailsHtml);
//     }
// }
// $(document).on('click', '.delete-data', function () {
//     const $dataItem = $(this).closest('.data-item');
//     const $dateContainer = $(this).closest('.date-container');
//
//     const id = $(this).data('id');
//     const type = $(this).data('type');
//
//     $dataItem.remove();
//     selectedData[type] = selectedData[type].filter(item => item.id !== id);
//
//     const remainingItemsCount = $dateContainer.find('.data-item').length;
//
//     if (remainingItemsCount === 0) {
//         $dateContainer.remove();
//     }
// });
//
// function handleAjaxError(jqXHR) {
//     if (jqXHR.status === 401) {
//         Swal.fire('Error!', '로그인이 되지 않았습니다.', 'error').then((result) => {
//             if (result.value) {
//                 window.location.href = '/login';
//             }
//         });
//     } else {
//         console.error('An error occurred:', jqXHR.statusText);
//     }
// }