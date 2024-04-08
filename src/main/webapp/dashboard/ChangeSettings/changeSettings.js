document.addEventListener('DOMContentLoaded', function() {
           	// Get token from localStorage
		    var token = JSON.parse(localStorage.getItem('authToken'));
			if(token == null){
				alert("You don't have a valid token. Redirecting to login.")
				window.location.href = '../../login/login.html'; // Redirect to logi
			}
		   
            document.querySelector('.change-settings-container form').addEventListener('submit', function(event) {
                event.preventDefault(); // Prevent the default form submission
                
                // Get form data
		        var username = document.getElementById('username').value;
		        var fullname = document.getElementById('fullname') ?  document.getElementById('fullname').value : '';
		        var email = document.getElementById('email') ?  document.getElementById('email').value : '';
		        var phoneNum = document.getElementById('phoneNumber') ?  document.getElementById('phoneNumber').value : '';
		        var occupation = document.getElementById('occupation') ? document.getElementById('occupation').value : '';
				var workplace = document.getElementById('workplace') ? document.getElementById('workplace').value : '';
				var address = document.getElementById('address') ? document.getElementById('address').value : '';
				var zipcode = document.getElementById('zipCode') ? document.getElementById('zipCode').value : '';
				var nif = document.getElementById('nif') ? document.getElementById('nif').value : '';
				
				var privacy = (document.querySelector('input[name="privacy"]:checked').value).toUpperCase();
				
		        // Construct the request body
		        var requestBody = {
		            "username": username,
		            "fullname": fullname,
		            "email": email,
		            "phoneNum": phoneNum,
		            "occupation": occupation,
		            "workplace": workplace,
		            "address": address,
		            "zipcode": zipcode,
		            "nif": nif,
		            "privacy": privacy,
		            "token":token
		        };

                // Send the form data to the server
                fetch('/rest/dashboard/changeSettings', {
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
                    console.log('Settings change successful:', data);
                    // Redirect to login page or show success message
                    window.location.href = '../dashboard.html';
                })
                .catch(error => {
                    // Handle error
                    console.error('There was a problem changing the settings:', error);	
                    alert("This user doesn't have permission to execute this operation.");
                    // Display error message to user
                });
            });
        });