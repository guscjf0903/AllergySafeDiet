class UploadFileManager {
    constructor() {
        this.selectedFiles = [];
    }

    handleFileSelect(event) {
        const files = event.target.files;
        for (let i = 0; i < files.length; i++) {
            const file = files[i];
            this.selectedFiles.push(file);
        }
        this.createPreviews();
    }

    handleDeletePreview(event) {
        const index = parseInt($(event.target).data('index'), 10);
        this.selectedFiles.splice(index, 1);
        this.createPreviews();
    }

    createPreviews() {
        $('#imagePreviewContainer').empty();
        this.selectedFiles.forEach((file, index) => {
            const reader = new FileReader();
            reader.onload = (e) => {
                const previewHtml = `
                    <div class="preview-wrapper me-2 position-relative" style="width: 35%; margin-top: 20px;">
                        <img src="${e.target.result}" class="img-fluid" alt="Image Preview">
                        <button type="button" class="btn-close position-absolute top-0 end-0" aria-label="Close" data-index="${index}"></button>
                    </div>
                `;
                $('#imagePreviewContainer').append(previewHtml);
            };
            reader.readAsDataURL(file);
        });
    }

    getSelectedFiles() {
        return this.selectedFiles;
    }
}
