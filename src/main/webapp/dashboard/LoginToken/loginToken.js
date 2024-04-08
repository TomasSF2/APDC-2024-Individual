document.addEventListener('DOMContentLoaded', function() {
    // Retrieve token from local storage
    var token = localStorage.getItem('authToken');

    // Display the token
    var tokenDiv = document.getElementById('token');

    if (token) {
        tokenDiv.textContent = token;
    } else {
        tokenDiv.textContent = 'No token found in local storage.';
    }

});
