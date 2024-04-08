document.addEventListener('DOMContentLoaded', function() {
	// Get token from localStorage
    var token = JSON.parse(localStorage.getItem('authToken'));
	if(token == null){
		alert("You don't have a valid token. Redirecting to login.")
		window.location.href = '../../login/login.html'; // Redirect to logi
	}
		
    document.querySelector('.change-password-container form').addEventListener('submit', function(event) {
        event.preventDefault(); // Prevent the default form submission
        
        // Get form data
        var currentPassword = document.getElementById('currentPassword').value;
        var newPassword = document.getElementById('newPassword').value;
        var newPasswordConfirm = document.getElementById('newPasswordConfirm').value;
        
        // Construct the request body
        var requestBody = {
            "currentPassword": currentPassword,
            "newPassword": newPassword,
            "newPasswordConfirm":newPasswordConfirm,
            "token": token

        };

        // Send the form data to the server
        fetch('/rest/dashboard/changePassword', {
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
            console.log('Password change successful:', data);
            // Redirect or show success message
			alert("Password change successful!");
			window.location.href = '../dashboard.html'; // Redirect to dashboard

        })
        .catch(error => {
            // Handle error
            console.error('There was a problem changing the password:', error);
            // Display error message to user
            alert("Wrong password or the new passwords don't match.");
        });
    });
});
