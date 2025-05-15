document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');

    if (loginForm) {
        loginForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const formData = new FormData(loginForm);
            fetch('/form-login', {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                showPopup(data.message, data.success);
                if (data.success) {
                    localStorage.setItem('userId', data.userId);
                    localStorage.setItem('role', formData.get('role'));
                    const role = formData.get('role');
                    if (role === 'PATIENT') window.location.href = '/patient-dashboard.html';
                    else if (role === 'DOCTOR') window.location.href = '/doctor-dashboard.html';
                    else if (role === 'ADMIN') window.location.href = '/admin-dashboard.html';
                    else if (role === 'NURSE') window.location.href = '/nurse-dashboard.html';
                    else if (role === 'ACCOUNTANT') window.location.href = '/accountant-dashboard.html';
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showPopup('An error occurred. Please try again.', false);
            });
        });
    }

    if (registerForm) {
        registerForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const formData = new FormData(registerForm);
            fetch('/registerPatient', {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                showPopup(data.message, data.success);
                if (data.success) {
                    setTimeout(() => {
                        // Add query parameter to ensure fresh load
                        window.location.href = '/login.html?registerSuccess=true';
                    }, 2000);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showPopup('An error occurred. Please try again.', false);
            });
        });
    }
});