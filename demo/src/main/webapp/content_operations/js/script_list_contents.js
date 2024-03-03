function fetchAndDisplayContents(title) {
    fetch(`http://localhost:8080/demo/rest/content/getByTitle/${title}`)
        .then(response => response.json())
        .then(contents => {
            const container = document.getElementById('content-list');
            container.innerHTML = ''; // Clear existing contents if any

            contents.forEach(content => {
                const element = document.createElement('div');
                element.textContent = content.title; 
                element.classList.add('content-item'); 
                element.setAttribute('data-id', content.id); 

                element.addEventListener('click', () => displayContentDetails(content));

                if (content.photoURL) {
                    const img = document.createElement('img');
                    img.src = content.photoURL;
                    img.alt = 'Photo of ' + content.title;
                    img.classList.add('thumbnail'); 
            
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

    const descriptionElement = document.createElement('p');
    descriptionElement.textContent = content.description;
    detailContainer.appendChild(descriptionElement);

    if (content.photoURL) {
        const img = document.createElement('img');
        img.src = content.photoURL;
        img.alt = 'Photo of ' + content.title;
        detailContainer.appendChild(img);
    }
}

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
