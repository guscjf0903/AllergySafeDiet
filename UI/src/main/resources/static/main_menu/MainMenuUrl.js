$(document).ready(function() {
    $('.menu-button').on('click', function() {
        window.location.href = '/food_health_data/select_date';
    });

    $('.health-analysis-button').on('click', function() {
        window.location.href = '/health-analysis';
    });

    $('.community-button').on('click', function() {
        window.location.href = '/community';
    });

    $('.allergy-info-button').on('click', function() {
        window.location.href = '/allergy_info';
    });
});