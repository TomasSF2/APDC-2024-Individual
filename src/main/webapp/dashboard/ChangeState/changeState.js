document.addEventListener('DOMContentLoaded', function() {
	// Get token from localStorage
    var token = JSON.parse(localStorage.getItem('authToken'));
	if(token == null){
		alert("You don't have a valid token. Redirecting to login.")
		window.location.href = '../../login/login.html'; // Redirect to logi
	}
		
    document.querySelector('.change-state-container form').addEventListener('submit', function(event) {
        event.preventDefault(); // Prevent the default form submission
        
        // Get form data
        var username = document.getElementById('username').value;
        
        // Construct the request body
        var requestBody = {
            "username": username,
            "token": token

        };

        // Send the form data to the server
        fetch('/rest/dashboard/changeState', {
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
            throw new Error('Network response was not ok');
        })
        .then(data => {
            // Handle success response
            console.log('State change successful:', data);
            // Redirect or show success message
			alert("State change successful!");
			window.location.href = '../dashboard.html'; // Redirect to dashboard

        })
        .catch(error => {
            // Handle error
            console.error('There was a problem changing the state:', error);
            // Display error message to user
            alert("You don't have permission to change this user's state or the user doesn't exist!");
        });
    });
});
