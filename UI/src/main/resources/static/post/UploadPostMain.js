$(document).ready(function () {
    const apiUrl = $('#apiUrl').data('url');

    const fileManager = new UploadFileManager();
    const dataFetcher = new FoodHealthDataFetcher(apiUrl);
    const postSubmitter = new PostSubmitter(apiUrl);

    $('#addDate').click(dataFetcher.fetchDataForDate.bind(dataFetcher));

    $('#postImages').change(fileManager.handleFileSelect.bind(fileManager));

    $('#imagePreviewContainer').on('click', '.btn-close', fileManager.handleDeletePreview.bind(fileManager));

    $('#submitPost').click(() => postSubmitter.submitPost(fileManager.getSelectedFiles(), dataFetcher.selectedData));
});
