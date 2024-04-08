// Select the container element
        const container = document.querySelector('.logout-container');
		
		localStorage.clear();
		
		// Function to hide the container and redirect after 5 seconds
        setTimeout(() => {
            container.style.display = 'none';
            // Redirect to login page
            window.location.href = '/../../login/login.html';
        }, 5000);
 