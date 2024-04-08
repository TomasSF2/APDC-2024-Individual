document.addEventListener('DOMContentLoaded', function() {
	
	// Get token from localStorage
    var token = JSON.parse(localStorage.getItem('authToken'));
	if(token == null){
		alert("You don't have a valid token. Redirecting to login.")
		window.location.href = '../../login/login.html'; // Redirect to logi
	}
    document.querySelector('.change-role-container form').addEventListener('submit', function(event) {
        event.preventDefault(); // Prevent the default form submission
        
        // Get form data
        var changingUsername = document.getElementById('username').value;
        var changingRole = document.getElementById('role').value;
      
        // Construct the request body
        var requestBody = {
            "username": changingUsername,
            "role": changingRole,
            "token": token

        };

        // Send the form data to the server
        fetch('/rest/dashboard/changeRole', {
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
            console.log('Role change successful:', data);
            // Redirect or show success message
			alert("Role change successful!");
			window.location.href = '../dashboard.html'; // Redirect to dashboard

        })
        .catch(error => {
            // Handle error
            console.error('There was a problem changing the role:', error);
            // Display error message to user
            alert("You don't have permission to change this user's role or the user doesn't exist!");
        });
    });
});
