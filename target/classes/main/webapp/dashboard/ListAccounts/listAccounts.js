document.addEventListener('DOMContentLoaded', function() {
	// Get token from localStorage
	var token = JSON.parse(localStorage.getItem('authToken'));
	if (token == null) {
		alert("You don't have a valid token. Redirecting to login.")
		window.location.href = '../../login/login.html'; // Redirect to logi
	}

	var requestBody = {
		"token": token
	};

	fetch('/rest/dashboard/listAccounts', {
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
			throw new Error('Failed to fetch accounts');
		})
		.then(data => {
			displayAccounts(data);
		})
		.catch(error => {
			console.error('Error fetching accounts:', error.message);
		});
});

function displayAccounts(users) {
	const userList = document.getElementById('userList');
	userList.innerHTML = ''; // Clear previous list

	users.forEach(user => {
		const listItem = document.createElement('li');
		let userInfo = `Username: ${user.username}, Email: ${user.email}, Fullname: ${user.fullname}`;

		// Check if additional fields are not null before adding them to userInfo
		if (user.phone != null) {
			userInfo += `, Phone: ${user.phone}`;
		}
		if (user.occupation != null) {
			userInfo += `, Occupation: ${user.occupation}`;
		}
		if (user.workplace != null) {
			userInfo += `, Workplace: ${user.workplace}`;
		}
		if (user.address != null) {
			userInfo += `, Address: ${user.address}`;
		}
		if (user.zipcode != null) {
			userInfo += `, Zip Code: ${user.zipcode}`;
		}
		if (user.nif != null) {
			userInfo += `, NIF: ${user.nif}`;
		}

		listItem.textContent = userInfo;
		userList.appendChild(listItem);
	});
}
