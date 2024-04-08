document.addEventListener('DOMContentLoaded', function() {
            document.querySelector('.register-container form').addEventListener('submit', function(event) {
                event.preventDefault(); // Prevent the default form submission
                
                // Get form data
		        var fullname = document.getElementById('fullname').value;
		        var username = document.getElementById('username').value;
		        var email = document.getElementById('email').value;
		        var phoneNum = document.getElementById('phoneNumber').value;
		        var password = document.getElementById('password').value;
				var confirmPassword = document.getElementById('confirmPassword').value;	
	
		        var occupation = document.getElementById('occupation') ? document.getElementById('occupation').value : '';
				var workplace = document.getElementById('workplace') ? document.getElementById('workplace').value : '';
				var address = document.getElementById('address') ? document.getElementById('address').value : '';
				var zipcode = document.getElementById('zipCode') ? document.getElementById('zipCode').value : '';
				var nif = document.getElementById('nif') ? document.getElementById('nif').value : '';

				var privacy = (document.querySelector('input[name="privacy"]:checked').value).toUpperCase();
				
		        // Construct the request body
		        var requestBody = {
		            "fullname": fullname,
		            "username": username,
		            "email": email,
		            "phoneNum": phoneNum,
		            "password": password,
		            "confirmPassword":confirmPassword,
		            "occupation": occupation,
		            "workplace": workplace,
		            "address": address,
		            "zipcode": zipcode,
		            "nif": nif,
		            "privacy": privacy
		        };

                // Send the form data to the server
                fetch('/rest/register', {
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
                    console.log('Registration successful:', data);
                    alert("Registration successful!");
                    // Redirect to login page or show success message
                    window.location.href = '../login/login.html'; // Redirect to login page
                })
                .catch(error => {
                    // Handle error
                    console.error('There was a problem with the registration:', error);
           	        alert("Wrong parameter or user already exists.");
                    // Display error message to user
                });
            });
        });