document.addEventListener('DOMContentLoaded', function() {
    document.querySelector('.login-container form').addEventListener('submit', function(event) {
        event.preventDefault(); // Prevent the default form submission
        // Get form data
        var username = document.getElementById('username').value;
        var password = document.getElementById('password').value;
        
        // Construct the request body
        var requestBody = {
            "username": username,
            "password": password
        };

        // Send the form data to the server
        fetch('/rest/login', { // Assuming the server is running on the same domain
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody)
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            
			alert('Login unsuccessful. Please check your username and password or if your account is active.');
            throw new Error('Wrong username or password');
        })
        .then(data => {

            // Handle success response
            console.log('Login successful:', data);

	        // Store the token in localStorage
	        localStorage.setItem('authToken', JSON.stringify(data));
			
            // Redirect to dashboard
           	window.location.href = '../dashboard/dashboard.html';
        })
        .catch(error => {
            // Handle error
            console.error('There was a problem with the login:', error);
            // Display error message to user
        });
    });
});