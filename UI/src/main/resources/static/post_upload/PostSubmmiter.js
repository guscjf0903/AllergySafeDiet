class PostSubmitter {
    constructor(apiUrl) {
        this.apiUrl = apiUrl;
    }

    submitPost(selectedFiles, selectedData) {
        const formData = new FormData();
        formData.append('title', $('#postTitle').val());
        formData.append('content', $('#postContent').val());
        const foodIds = selectedData.food.map(food => food.id);
        foodIds.forEach(id => {
            formData.append('foodIds', id);
        });
        const healthIds = selectedData.health.map(health => health.id);
        healthIds.forEach(id => {
            formData.append('healthIds', id);
        });
        selectedFiles.forEach((file, index) => {
            formData.append(`images`, file);
        });
        console.log(selectedFiles.length);
        const allImages = formData.getAll('images[0]');
        const allImages2 = formData.getAll('images[1]');

        console.log(allImages);
        console.log(allImages2);

        $.ajax({
            url: this.apiUrl + "/post/upload",
            method: 'POST',
            headers: {
                'Authorization': sessionStorage.getItem("loginToken"),
            },
            processData: false,
            contentType: false,
            data: formData,
            success: () => {
                alert("게시물을 업로드 하였습니다.");
                window.location.href = '/list';
            },
            error: (jqXHR) => {
                handleAjaxError(jqXHR);
            }
        });
    }
}
