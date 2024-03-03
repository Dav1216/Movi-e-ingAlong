// Function to fetch Content objects based on title and update the DOM
function fetchAndDisplayContents(title) {
    fetch(`http://localhost:8080/demo/rest/content/getByTitle/${title}`)
        .then(response => response.json())
        .then(contents => {
            const container = document.getElementById('content-list');
            container.innerHTML = ''; // Clear existing contents if any

            // Loop through each Content object and append it to the container
            contents.forEach(content => {
                const element = document.createElement('div');
                element.textContent = content.title; // Assuming 'title' is a property of Content
                element.classList.add('content-item'); // Add class for styling and identification
                element.setAttribute('data-id', content.id); // Store content ID for retrieval

                // Add click event listener for each content item
                element.addEventListener('click', () => displayContentDetails(content));

                // Check if the photo property exists and is not null
                if (content.photoURL) {
                    const img = document.createElement('img');
                    img.src = content.photoURL;
                    img.alt = 'Photo of ' + content.title;
                    img.classList.add('thumbnail'); /* Add a specific class for targeting */
            
                    // // Event Listeners
                    // img.addEventListener('mouseover', () => img.classList.add('enlarged'));
                    // img.addEventListener('mouseout', () => img.classList.remove('enlarged'));
            
                    element.appendChild(img);
                }

                container.appendChild(element);
            });
        })
        .catch(error => console.error('Error fetching contents:', error));
}

// Function to display the details of a clicked content
function displayContentDetails(content) {
    const detailContainer = document.getElementById('content-detail');
    detailContainer.innerHTML = ''; // Clear previous content details if any

    // Dynamically create and append elements to display content details
    const titleElement = document.createElement('h2');
    titleElement.textContent = content.title;
    detailContainer.appendChild(titleElement);

    // Example for displaying other properties
    const descriptionElement = document.createElement('p');
    descriptionElement.textContent = content.description;
    detailContainer.appendChild(descriptionElement);

    // Continue for other properties like genre, status, releaseDate, etc.

    // Check for photoURL and append if exists
    if (content.photoURL) {
        const img = document.createElement('img');
        img.src = content.photoURL;
        img.alt = 'Photo of ' + content.title;
        detailContainer.appendChild(img);
    }
}

// Example of how to call the function with a user-provided title
document.addEventListener('DOMContentLoaded', function() {
    const fetchButton = document.getElementById('fetchButton');
    fetchButton.addEventListener('click', function() {
        const title = document.getElementById('titleInput').value;
        if (title) {
            fetchAndDisplayContents(title);
        } else {
            alert('Please enter a title.');
        }
    });
});
