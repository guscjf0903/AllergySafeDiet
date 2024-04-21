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

        $.ajax({
            url: this.apiUrl + "/post/upload",
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem("accessToken"),
            },
            processData: false,
            contentType: false,
            data: formData,
            success: () => {
                alert("게시물을 업로드 하였습니다.");
                window.location.href = '/list';
            },
            error: (jqXHR) => {
                if (jqXHR.status === 401) {  // 토큰 만료 감지
                    refreshAccessToken(() => {
                        this.submitPost(selectedFiles, selectedData);  // 토큰 갱신 후 요청 재시도
                    });
                } else {
                    console.error(jqXHR);
                }
            }
        });
    }
}
