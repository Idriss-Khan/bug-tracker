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