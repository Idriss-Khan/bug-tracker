
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

// Bug table tags
document.addEventListener("DOMContentLoaded", function () {
    const statusCells = document.querySelectorAll(".bug-cell.status");
    const priorityCells = document.querySelectorAll(".bug-cell.priority");

    statusCells.forEach(function (cell) {
        const status = cell.textContent.trim();
        if (status === "New") {
            cell.classList.add("status-new");
        } else if (status === "In-Progress") {
            cell.classList.add("status-in-progress");
        } else if (status === "Resolved") {
            cell.classList.add("status-resolved");
        } else if (status === "Closed") {
            cell.classList.add("status-closed");
        }
        else if (status === "Completed") {
            cell.classList.add("status-resolved");
        }
    });

    priorityCells.forEach(function (cell) {
        const priority = cell.textContent.trim();
        if (priority === "Low") {
            cell.classList.add("priority-low");
        } else if (priority === "Medium") {
            cell.classList.add("priority-medium");
        } else if (priority === "High") {
            cell.classList.add("priority-high");
        }
    });
});

// User search and filter
// Get the input, role filter, and user rows
const searchInput = document.getElementById("user-search");
const roleFilter = document.getElementById("role-filter");
const userRows = document.querySelectorAll(".custom-row");

// Add event listeners to the search input and role filter
searchInput.addEventListener("input", filterUsers);
roleFilter.addEventListener("change", filterUsers);

// Function to filter users based on search term and role filter
function filterUsers() {
    const searchTerm = searchInput.value.toLowerCase();
    const selectedRole = roleFilter.value.toLowerCase();

    userRows.forEach((userRow) => {
        const firstNameCell = userRow.querySelector(".custom-cell:nth-child(2)");
        const lastNameCell = userRow.querySelector(".custom-cell:nth-child(3)");
        const roleCell = userRow.querySelector(".custom-cell:nth-child(6)");
        const firstName = firstNameCell.textContent.toLowerCase();
        const lastName = lastNameCell.textContent.toLowerCase();
        const role = roleCell.textContent.toLowerCase();

        const nameMatch = firstName.includes(searchTerm) || lastName.includes(searchTerm);
        const roleMatch = selectedRole === "" || role.includes(selectedRole);

        if (nameMatch && roleMatch) {
            userRow.style.display = "flex"; // Show matching rows
        } else {
            userRow.style.display = "none"; // Hide non-matching rows
        }
    });
}