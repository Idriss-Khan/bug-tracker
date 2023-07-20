
// Side Bar expanding and collapsing
$(document).ready(function () {
    $(".sidebar_btn").click(function () {
        $(".sidebar").toggleClass("active");
        $(".home_content").toggleClass("active");
    });
});

// Expand and collapsing table row
function toggleTable(row) {
    const content = row.nextElementSibling;
    content.style.display = content.style.display === "none" ? "block" : "none";
}