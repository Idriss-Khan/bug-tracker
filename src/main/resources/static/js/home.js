// Bug image upload function - displays selected screenshots
document.addEventListener("DOMContentLoaded", function () {
    const imagePreview = document.getElementById("imagePreview");
    const imagesInput = document.getElementById("bugImages");

    imagesInput.addEventListener("change", function () {
        imagePreview.innerHTML = ""; // Clear previous previews

        const files = this.files;
        for (let i = 0; i < files.length; i++) {
            const file = files[i];
            const reader = new FileReader();

            reader.onload = function (e) {
                const img = document.createElement("img");
                img.src = e.target.result;
                img.classList.add("img-thumbnail", "m-2");
                imagePreview.appendChild(img);
            }

            reader.readAsDataURL(file);
        }
    });
});

// Changes status colours for list of bugs
document.addEventListener("DOMContentLoaded", function() {
    var badgeElements = document.querySelectorAll(".badge.bug-status");

    badgeElements.forEach(function(badge) {
        var status = badge.textContent.trim();
        switch (status) {
            case "New":
                badge.classList.add("bg-primary");
                break;
            case "In-Progress":
                badge.classList.add("bg-warning");
                break;
            case "Resolved":
                badge.classList.add("bg-success");
                break;
            case "Closed":
                badge.classList.add("bg-secondary");
                break;
            default:
                badge.classList.add("bg-light");
                break;
        }
    });
});

// User profile page - changes user profile image
function previewProfileImage(event) {
    const input = event.target;
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function (e) {
            const profileImage = document.querySelector('.profile-image');
            profileImage.src = e.target.result;
            showActionIcons(); // Show action-icons when profile image is uploaded
        };
        reader.readAsDataURL(input.files[0]);
    }
}

function showActionIcons() {
    const actionIcons = document.querySelector('.action-icons');
    actionIcons.style.display = 'block'; // Display the action-icons
}

// Initially hides the action-icons
document.addEventListener('DOMContentLoaded', function () {
    const actionIcons = document.querySelector('.action-icons');
    actionIcons.style.display = 'none';
});

// Search and Filter JS

const statusFilter = document.getElementById('statusFilter');
const bugCards = document.querySelectorAll('.bug-card');
const bugSearchInput = document.getElementById('bugSearch');

bugSearchInput.addEventListener('input', () => {
    const searchQuery = bugSearchInput.value.toLowerCase();

    bugCards.forEach(card => {
        const cardTitle = card.querySelector('.card-title').textContent.toLowerCase();
        const cardStatus = card.classList.contains(statusFilter.value);
        const titleMatches = cardTitle.includes(searchQuery);

        if ((statusFilter.value === '' || cardStatus) && titleMatches) {
            card.style.display = 'block';
        } else {
            card.style.display = 'none';
        }
    });
});

statusFilter.addEventListener('change', () => {
    const selectedStatus = statusFilter.value;

    bugCards.forEach(card => {
        const cardStatus = card.classList.contains(selectedStatus);
        const cardTitle = card.querySelector('.card-title').textContent.toLowerCase();
        const titleMatches = cardTitle.includes(bugSearchInput.value.toLowerCase());

        if ((selectedStatus === '' || cardStatus) && titleMatches) {
            card.style.display = 'block';
        } else {
            card.style.display = 'none';
        }
    });
});