function displayPostDetails(data) {
    $('#postTitle').text(data.title);
    $('#postContent').text(data.content);
    $('#postAuthor').text(data.author);
    $('#postDate').text(data.date);
    $('#postViews').text(data.views);

    if (data.images && data.images.length > 0) {
        let carouselInner = $('<div class="carousel-inner"></div>');
        data.images.forEach((image, index) => {
            loadImage(image, index, carouselInner);
            // loadImageAndMeasureSize(image, index, carouselInner)
        });
        $('#postImages').empty().append(carouselInner);
        $('#postImages').append(carouselControls());
    }

    if (data.healthDataList && data.healthDataList.length > 0) {
        let healthTableRows = data.healthDataList.map(health => {
            let pillsList = health.pills.map(pill =>
                `<li>${pill.name} - (${pill.count}개)</li>`
            ).join('');
            pillsList = `<ul>${pillsList}</ul>`;

            return `
            <tr>
                <td>${health.date}</td>
                <td>${health.allergiesStatus}</td>
                <td>${health.conditionStatus}</td>
                <td>${health.weight}kg</td>
                <td>${health.sleepTime}시간</td>
                <td>${health.healthNotes}</td>
                <td>${pillsList}</td> 
            </tr>
        `;
        }).join('');

        $('#healthData').html(`
        <thead>
            <tr>
                <th>날짜</th>
                <th>알러지 상태</th>
                <th>건강 상태</th>
                <th>체중</th>
                <th>수면 시간</th>
                <th>특이사항</th>
                <th>알약 정보</th> 
            </tr>
        </thead>
        <tbody>
            ${healthTableRows}
        </tbody>
    `);
    }

    if (data.foodDataList && data.foodDataList.length > 0) {
        let foodTableRows = data.foodDataList.map(food => {
            let ingredientsList = food.ingredients.map(
                ingredient => ingredient.ingredientName).join(', ');

            ingredientsList = `<ul>${ingredientsList}</ul>`;
            return`
            <tr>
                <td>${food.date}</td>
                <td>${food.mealType}</td>
                <td>${food.mealTime}</td>
                <td>${food.foodName}</td>
                <td>${ingredientsList}</td>                
                <td>${food.foodNotes}</td>
            </tr>
        `;
        }).join('');

        $('#foodData').html(`
            <thead>
                <tr>
                    <th>날짜</th>
                    <th>식사 종류</th>
                    <th>식사 시간</th>
                    <th>음식 이름</th>
                    <th>원재료</th>
                    <th>특이사항</th>
                </tr>
            </thead>
            <tbody>
                ${foodTableRows}
            </tbody>
        `);
    }

    if (data.isWriter === true) {
        $('#editPost').show();
        $('#deletePost').show();
    } else {
        $('#editPost').hide();
        $('#deletePost').hide();
    }
}
function loadImage(imageUrl, index, carouselInner) {
    let itemClass = index === 0 ? 'carousel-item active' : 'carousel-item';
    let carouselItem = $(`
        <div class="${itemClass}">
            <img src="${imageUrl}" class="d-block w-100" alt="Image" style="max-width: 50%; height: auto;">
        </div>
    `);
    carouselInner.append(carouselItem);

    let startTime = Date.now();
    carouselItem.find('img').on('load', function() {
        let loadTime = Date.now() - startTime;
        console.log(`Image ${index + 1} loaded in ${loadTime} ms`);
    }).on('error', function() {
        console.log(`Error loading image ${index + 1}.`);
    });
}

function carouselControls() {
    return `
        <a class="carousel-control-prev" href="#postImages" role="button" data-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="sr-only">Previous</span>
        </a>
        <a class="carousel-control-next" href="#postImages" role="button" data-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="sr-only">Next</span>
        </a>
    `;
}

function loadImageAndMeasureSize(imageUrl, index, carouselInner) {
    let itemClass = index === 0 ? 'carousel-item active' : 'carousel-item';
    let carouselItem = $(`
        <div class="${itemClass}">
            <img src="${imageUrl}" class="d-block w-100" alt="Image" style="max-width: 50%; height: auto;">
        </div>
    `);
    carouselInner.append(carouselItem);

    fetch(imageUrl)
        .then(response => response.blob())
        .then(blob => {
            console.log(`Image ${index + 1} loaded, size: ${blob.size} bytes`);
        })
        .catch(err => console.log(`Error loading image ${index + 1}:`, err));
}
