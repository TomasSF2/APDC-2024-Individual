document.addEventListener('DOMContentLoaded', function() {
			localStorage.clear();
			
	        fetch('/rest/bootstrap', {
	            method: 'POST',
	            headers: {
	                'Content-Type': 'application/json'
	            }
	        })
	        .then(response => {
	            if (response.ok) {
	                console.log('Bootstrap success');
	            } else {
	                console.error('Bootstrap failed');
	            }
	        })
	        .catch(error => {
	            console.error('Bootstrap failed:', error);
	        });
	    });
