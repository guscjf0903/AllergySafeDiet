function createPreviews() {
    selectedFiles.forEach((file, index) => {
        const reader = new FileReader();
        reader.onload = function(e) {
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