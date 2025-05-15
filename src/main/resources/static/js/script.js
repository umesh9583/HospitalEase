document.addEventListener('DOMContentLoaded', () => {
    const contentDiv = document.getElementById('content');
    const navLinks = document.querySelectorAll('.nav-link');
    const popup = document.getElementById('popup');
    const popupIcon = document.getElementById('popup-icon');
    const popupMessage = document.getElementById('popup-message');
    const popupClose = document.getElementById('popup-close');
    const loading = document.getElementById('loading');
    const toastContainer = document.getElementById('toast-container');
    let appointments = [];
    let currentPage = 1;
    const perPage = 5;
    let searchQuery = '';
    let dateFrom = '';
    let dateTo = '';

    // Show popup
    function showPopup(message, isSuccess) {
        popup.className = `popup ${isSuccess ? 'success' : 'error'} show`;
        popupIcon.textContent = isSuccess ? '✅' : '❌';
        popupMessage.textContent = message;
        setTimeout(() => {
            popup.className = 'popup';
        }, 5000);
    }

    // Show toast notification
    function showToast(message, isSuccess) {
        const toast = document.createElement('div');
        toast.className = `toast ${isSuccess ? 'success' : 'error'}`;
        toast.innerHTML = `
            <span class="icon">${isSuccess ? '✅' : '❌'}</span>
            <span>${message}</span>
        `;
        toastContainer.appendChild(toast);
        setTimeout(() => {
            toast.remove();
        }, 3000);
    }

    // Close popup
    popupClose.addEventListener('click', () => {
        popup.className = 'popup';
    });

    // Show loading spinner
    function showLoading(show) {
        loading.className = show ? 'fixed inset-0 bg-gray-900 bg-opacity-75 flex items-center justify-center z-50' : 'hidden';
    }

    // Navigate to module
    function navigate(hash) {
        const modules = document.querySelectorAll('.module');
        modules.forEach(module => module.classList.add('hidden'));
        const target = document.getElementById(hash.substring(1));
        if (target) {
            target.classList.remove('hidden');
            contentDiv.classList.add('opacity-0');
            setTimeout(() => {
                contentDiv.classList.remove('opacity-0');
            }, 50);
        }
        navLinks.forEach(link => link.classList.remove('active'));
        const activeLink = document.querySelector(`.nav-link[href="${hash}"]`);
        if (activeLink) activeLink.classList.add('active');
    }

    // Handle routing
    function handleRoute() {
        const hash = window.location.hash || '#home'; // Default to home
        navigate(hash);
    }

    // Get Started button
    document.getElementById('get-started').addEventListener('click', () => {
        window.location.hash = '#home';
    });

    // Load dashboard stats
    async function loadDashboardStats() {
        try {
            showLoading(true);
            const patientsResponse = await fetch('http://localhost:8080/api/patients');
            const doctorsResponse = await fetch('http://localhost:8080/api/doctors');
            const statsResponse = await fetch('http://localhost:8080/api/appointments/stats');
            const patients = await patientsResponse.json();
            const doctors = await doctorsResponse.json();
            const stats = await statsResponse.json();
            document.getElementById('total-patients').textContent = patients.length;
            document.getElementById('total-doctors').textContent = doctors.length;
            document.getElementById('total-appointments').textContent = stats.totalAppointments;
            document.getElementById('today-appointments').textContent = stats.todayAppointments;
            // Quick Access Stats
            document.getElementById('quick-total-patients').textContent = patients.length;
            document.getElementById('quick-total-doctors').textContent = doctors.length;
            document.getElementById('quick-today-appointments').textContent = stats.todayAppointments;
            // Placeholder for Medicines in Stock and Pending Bills
            document.getElementById('quick-medicines-stock').textContent = 'N/A';
            document.getElementById('quick-pending-bills').textContent = 'N/A';
        } catch (error) {
            showPopup('Failed to load dashboard stats.', false);
        } finally {
            showLoading(false);
        }
    }

    // Load patients
    async function loadPatients() {
        try {
            showLoading(true);
            const response = await fetch('http://localhost:8080/api/patients');
            const patients = await response.json();
            const patientSelect = document.getElementById('patientId');
            const editPatientSelect = document.getElementById('edit-patient-id');
            patientSelect.innerHTML = '<option value="">Select a patient</option>';
            editPatientSelect.innerHTML = '<option value="">Select a patient</option>';
            patients.forEach(patient => {
                const option = document.createElement('option');
                option.value = patient.id;
                option.textContent = `${patient.name} (${patient.phone || 'No phone'})`;
                patientSelect.appendChild(option.cloneNode(true));
                editPatientSelect.appendChild(option);
            });
        } catch (error) {
            showPopup('Failed to load patients.', false);
        } finally {
            showLoading(false);
        }
    }

    // Load doctors
    async function loadDoctors() {
        try {
            showLoading(true);
            const response = await fetch('http://localhost:8080/api/doctors');
            const doctors = await response.json();
            const doctorSelect = document.getElementById('doctorId');
            const doctorAssignSelect = document.getElementById('doctorIdAssign');
            doctorSelect.innerHTML = '<option value="">Select a doctor</option>';
            doctorAssignSelect.innerHTML = '<option value="">Select a doctor</option>';
            doctors.forEach(doctor => {
                const option = document.createElement('option');
                option.value = doctor.id;
                option.textContent = `${doctor.name} (${doctor.specialization})`;
                doctorSelect.appendChild(option.cloneNode(true));
                doctorAssignSelect.appendChild(option);
            });
        } catch (error) {
            showPopup('Failed to load doctors.', false);
        } finally {
            showLoading(false);
        }
    }

    // Load appointments for assign
    async function loadAppointmentsForAssign() {
        try {
            showLoading(true);
            const response = await fetch('http://localhost:8080/api/appointments');
            const appointments = await response.json();
            const appointmentSelect = document.getElementById('appointmentId');
            appointmentSelect.innerHTML = '<option value="">Select an appointment</option>';
            appointments.forEach(appointment => {
                const option = document.createElement('option');
                option.value = appointment.id;
                option.textContent = `ID: ${appointment.id} (Patient ID: ${appointment.patientId}, ${appointment.appointmentDate}, ${appointment.status})`;
                appointmentSelect.appendChild(option);
            });
        } catch (error) {
            showPopup('Failed to load appointments.', false);
        } finally {
            showLoading(false);
        }
    }

    // Load appointments for list
    async function loadAppointments() {
        try {
            showLoading(true);
            const response = await fetch('http://localhost:8080/api/appointments');
            appointments = await response.json();
            const patientResponse = await fetch('http://localhost:8080/api/patients');
            const doctorResponse = await fetch('http://localhost:8080/api/doctors');
            const patients = await patientResponse.json();
            const doctors = await doctorResponse.json();

            const patientMap = new Map(patients.map(p => [p.id, p.name]));
            const doctorMap = new Map(doctors.map(d => [d.id, `${d.name} (${d.specialization})`]));

            renderAppointments(patientMap, doctorMap);
        } catch (error) {
            showPopup('Failed to load appointments.', false);
        } finally {
            showLoading(false);
        }
    }

    // Render appointments
    function renderAppointments(patientMap, doctorMap) {
        const tbody = document.getElementById('appointment-table');
        const sort = document.getElementById('sort').value;

        // Filter appointments
        let filteredAppointments = appointments.filter(appointment => {
            const patientName = patientMap.get(appointment.patientId) || '';
            const matchesSearch = patientName.toLowerCase().includes(searchQuery.toLowerCase());
            const appointmentDate = new Date(appointment.appointmentDate);
            const fromDate = dateFrom ? new Date(dateFrom) : null;
            const toDate = dateTo ? new Date(dateTo) : null;
            const matchesDate = (!fromDate || appointmentDate >= fromDate) && (!toDate || appointmentDate <= new Date(toDate + 'T23:59:59'));
            return matchesSearch && matchesDate;
        });

        // Sort appointments
        filteredAppointments.sort((a, b) => {
            if (sort === 'id') return a.id - b.id;
            if (sort === 'date') return new Date(a.appointmentDate) - new Date(b.appointmentDate);
            if (sort === 'patient') return patientMap.get(a.patientId).localeCompare(patientMap.get(b.patientId));
            if (sort === 'doctor') {
                const doctorA = a.doctor ? doctorMap.get(a.doctor.id) : 'Not Assigned';
                const doctorB = b.doctor ? doctorMap.get(b.doctor.id) : 'Not Assigned';
                return doctorA.localeCompare(doctorB);
            }
            if (sort === 'status') return a.status.localeCompare(b.status);
        });

        // Paginate
        const start = (currentPage - 1) * perPage;
        const end = start + perPage;
        const paginated = filteredAppointments.slice(start, end);

        // Render table
        tbody.innerHTML = '';
        paginated.forEach((appointment, index) => {
            const row = document.createElement('tr');
            row.className = 'animate-row-slide';
            row.style.animationDelay = `${index * 100}ms`;
            const statusClass = {
                'Scheduled': 'status-scheduled',
                'Completed': 'status-completed',
                'Canceled': 'status-canceled'
            }[appointment.status] || 'status-scheduled';
            row.innerHTML = `
                <td class="p-3">${appointment.id}</td>
                <td class="p-3">${patientMap.get(appointment.patientId) || 'Unknown'}</td>
                <td class="p-3">${appointment.doctor ? doctorMap.get(appointment.doctor.id) : 'Not Assigned'}</td>
                <td class="p-3">${new Date(appointment.appointmentDate).toLocaleString()}</td>
                <td class="p-3"><span class="status-badge ${statusClass}">${appointment.status}</span></td>
                <td class="p-3 flex space-x-2">
                    ${appointment.status === 'Scheduled' ? `<button class="btn btn-danger btn-cancel" data-id="${appointment.id}">Cancel</button>` : ''}
                    ${appointment.status === 'Scheduled' ? `<button class="btn btn-success btn-complete" data-id="${appointment.id}">Complete</button>` : ''}
                </td>
            `;
            tbody.appendChild(row);
        });

        document.getElementById('page-info').textContent = `Page ${currentPage} of ${Math.ceil(filteredAppointments.length / perPage) || 1}`;
        document.getElementById('prev-page').disabled = currentPage === 1;
        document.getElementById('next-page').disabled = end >= filteredAppointments.length;
    }

    // Export to CSV
    function exportToCSV(appointments, patientMap, doctorMap) {
        const headers = ['ID,Patient,Doctor,Date,Status'];
        const rows = appointments.map(appointment => [
            appointment.id,
            `"${patientMap.get(appointment.patientId) || 'Unknown'}"`,
            `"${appointment.doctor ? doctorMap.get(appointment.doctor.id) : 'Not Assigned'}"`,
            `"${new Date(appointment.appointmentDate).toLocaleString()}"`,
            `"${appointment.status}"`
        ].join(','));
        const csv = [...headers, ...rows].join('\n');
        const blob = new Blob([csv], { type: 'text/csv' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'appointments.csv';
        a.click();
        URL.revokeObjectURL(url);
    }

    // Register form submission
    document.getElementById('register-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = {
            name: formData.get('name'),
            guardianName: formData.get('guardianName'),
            gender: formData.get('gender'),
            address: formData.get('address'),
            phone: formData.get('phone'),
            email: formData.get('email') || null
        };

        if (!data.gender) {
            showPopup('Please select a gender.', false);
            return;
        }

        try {
            showLoading(true);
            const response = await fetch('http://localhost:8080/api/patients', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
            if (response.ok) {
                showPopup('Registration Successful!', true);
                showToast('Patient registered!', true);
                e.target.reset();
                loadPatients();
                loadDashboardStats();
            } else {
                const error = await response.json();
                showPopup(`Registration Failed: ${error.message || 'Unknown error'}`, false);
            }
        } catch (error) {
            showPopup(`Registration Failed: ${error.message}`, false);
        } finally {
            showLoading(false);
        }
    });

    // Edit patient form submission
    document.getElementById('edit-patient-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const patientId = formData.get('patientId');
        const data = {
            name: formData.get('name'),
            guardianName: formData.get('guardianName'),
            gender: formData.get('gender'),
            address: formData.get('address'),
            phone: formData.get('phone'),
            email: formData.get('email') || null
        };

        if (!data.gender) {
            showPopup('Please select a gender.', false);
            return;
        }

        try {
            showLoading(true);
            const response = await fetch(`http://localhost:8080/api/patients/${patientId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
            if (response.ok) {
                showPopup('Patient Updated Successfully!', true);
                showToast('Patient details updated!', true);
                e.target.reset();
                loadPatients();
                loadAppointments();
                loadDashboardStats();
            } else {
                const error = await response.json();
                showPopup(`Update Failed: ${error.message || 'Unknown error'}`, false);
            }
        } catch (error) {
            showPopup(`Update Failed: ${error.message}`, false);
        } finally {
            showLoading(false);
        }
    });

    // Populate edit form
    document.getElementById('edit-patient-id').addEventListener('change', async (e) => {
        const patientId = e.target.value;
        if (!patientId) return;
        try {
            showLoading(true);
            const response = await fetch(`http://localhost:8080/api/patients`);
            const patients = await response.json();
            const patient = patients.find(p => p.id === parseInt(patientId));
            if (patient) {
                document.getElementById('edit-name').value = patient.name;
                document.getElementById('edit-guardianName').value = patient.guardianName;
                document.getElementById('edit-gender').value = patient.gender;
                document.getElementById('edit-address').value = patient.address;
                document.getElementById('edit-phone').value = patient.phone;
                document.getElementById('edit-email').value = patient.email || '';
            }
        } catch (error) {
            showPopup('Failed to load patient details.', false);
        } finally {
            showLoading(false);
        }
    });

    // Schedule form submission
    document.getElementById('schedule-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const patientId = document.getElementById('patientId').value;
        const doctorId = document.getElementById('doctorId').value;
        const appointmentDate = document.getElementById('appointmentDate').value;

        try {
            showLoading(true);
            const response = await fetch('http://localhost:8080/api/appointments', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    patientId,
                    doctor: { id: doctorId },
                    appointmentDate
                })
            });
            const responseData = await response.json();
            if (response.ok) {
                showPopup('Appointment scheduled successfully!', true);
                showToast('Appointment scheduled!', true);
                e.target.reset();
                loadAppointmentsForAssign();
                loadAppointments();
                loadDashboardStats();
            } else {
                showPopup(responseData || 'Failed to schedule appointment.', false);
            }
        } catch (error) {
            showPopup('Error scheduling appointment: ' + error.message, false);
        } finally {
            showLoading(false);
        }
    });

    // Assign form submission
    document.getElementById('assign-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const appointmentId = document.getElementById('appointmentId').value;
        const doctorId = document.getElementById('doctorIdAssign').value;

        try {
            showLoading(true);
            const response = await fetch('http://localhost:8080/api/appointments/assign', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ appointmentId, doctorId })
            });
            if (response.ok) {
                showPopup('Doctor assigned successfully!', true);
                showToast('Doctor assigned!', true);
                e.target.reset();
                loadAppointmentsForAssign();
                loadAppointments();
            } else {
                const error = await response.json();
                showPopup(error.message || 'Failed to assign doctor.', false);
            }
        } catch (error) {
            showPopup('Error assigning doctor: ' + error.message, false);
        } finally {
            showLoading(false);
        }
    });

    // Cancel or complete appointment
    document.getElementById('appointment-table').addEventListener('click', async (e) => {
        const appointmentId = e.target.dataset.id;
        if (!appointmentId) return;

        if (e.target.classList.contains('btn-cancel')) {
            if (confirm('Are you sure you want to cancel this appointment?')) {
                try {
                    showLoading(true);
                    const response = await fetch(`http://localhost:8080/api/appointments/${appointmentId}/cancel`, {
                        method: 'POST'
                    });
                    if (response.ok) {
                        showPopup('Appointment canceled successfully!', true);
                        showToast('Appointment canceled!', true);
                        loadAppointments();
                        loadAppointmentsForAssign();
                        loadDashboardStats();
                    } else {
                        const error = await response.json();
                        showPopup(error.message || 'Failed to cancel appointment.', false);
                    }
                } catch (error) {
                    showPopup('Error canceling appointment: ' + error.message, false);
                } finally {
                    showLoading(false);
                }
            }
        } else if (e.target.classList.contains('btn-complete')) {
            if (confirm('Mark this appointment as completed?')) {
                try {
                    showLoading(true);
                    const response = await fetch(`http://localhost:8080/api/appointments/${appointmentId}/complete`, {
                        method: 'POST'
                    });
                    if (response.ok) {
                        showPopup('Appointment marked as completed!', true);
                        showToast('Appointment completed!', true);
                        loadAppointments();
                        loadAppointmentsForAssign();
                        loadDashboardStats();
                    } else {
                        const error = await response.json();
                        showPopup(error.message || 'Failed to complete appointment.', false);
                    }
                } catch (error) {
                    showPopup('Error completing appointment: ' + error.message, false);
                } finally {
                    showLoading(false);
                }
            }
        }
    });

    // Search and filter
    document.getElementById('search').addEventListener('input', (e) => {
        searchQuery = e.target.value;
        currentPage = 1;
        loadAppointments();
    });

    document.getElementById('date-from').addEventListener('change', (e) => {
        dateFrom = e.target.value;
        currentPage = 1;
        loadAppointments();
    });

    document.getElementById('date-to').addEventListener('change', (e) => {
        dateTo = e.target.value;
        currentPage = 1;
        loadAppointments();
    });

    // Sort and pagination
    document.getElementById('sort').addEventListener('change', () => {
        currentPage = 1;
        loadAppointments();
    });

    document.getElementById('prev-page').addEventListener('click', () => {
        if (currentPage > 1) {
            currentPage--;
            loadAppointments();
        }
    });

    document.getElementById('next-page').addEventListener('click', () => {
        if (currentPage < Math.ceil(appointments.length / perPage)) {
            currentPage++;
            loadAppointments();
        }
    });

    // Export CSV
    document.getElementById('export-csv').addEventListener('click', async () => {
        try {
            showLoading(true);
            const patientResponse = await fetch('http://localhost:8080/api/patients');
            const doctorResponse = await fetch('http://localhost:8080/api/doctors');
            const patients = await patientResponse.json();
            const doctors = await doctorResponse.json();
            const patientMap = new Map(patients.map(p => [p.id, p.name]));
            const doctorMap = new Map(doctors.map(d => [d.id, `${d.name} (${d.specialization})`]));
            exportToCSV(appointments, patientMap, doctorMap);
            showPopup('Appointments exported to CSV!', true);
            showToast('CSV exported!', true);
        } catch (error) {
            showPopup('Failed to export appointments.', false);
        } finally {
            showLoading(false);
        }
    });

    // Signup form submission
    document.getElementById('signup-form').addEventListener('submit', (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const password = formData.get('password');
        const confirmPassword = formData.get('confirmPassword');

        if (password !== confirmPassword) {
            showPopup('Passwords do not match.', false);
            return;
        }

        showPopup('Signup Successful! Please login.', true);
        showToast('Patient signed up!', true);
        e.target.reset();
        window.location.hash = '#login';
    });

    // Login form submission
    document.getElementById('login-form').addEventListener('submit', (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const role = formData.get('role');

        showPopup(`Login Successful as ${role}!`, true);
        showToast(`Logged in as ${role}!`, true);

        // Redirect based on role (placeholder for now)
        if (role === 'Patient') {
            window.location.hash = '#dashboard';
        } else {
            window.location.hash = '#dashboard'; // Update later for other roles
        }
    });

    // Contact form submission
    document.getElementById('contact-form').addEventListener('submit', (e) => {
        e.preventDefault();
        showPopup('Message Sent Successfully!', true);
        showToast('Message sent!', true);
        e.target.reset();
    });

    // Initialize
    window.addEventListener('hashchange', handleRoute);
    handleRoute();
    loadDashboardStats();
    loadPatients();
    loadDoctors();
    loadAppointmentsForAssign();
    loadAppointments();

    // Enhanced particle animation
    const particles = document.querySelector('.particles');
    for (let i = 0; i < 50; i++) {
        const particle = document.createElement('div');
        particle.className = 'absolute bg-white opacity-30 rounded-full';
        particle.style.width = `${Math.random() * 3 + 1}px`;
        particle.style.height = particle.style.width;
        particle.style.left = `${Math.random() * 100}%`;
        particle.style.top = `${Math.random() * 100}%`;
        particle.style.animation = `float ${Math.random() * 10 + 5}s infinite ease-in-out`;
        particle.style.animationDelay = `-${Math.random() * 10}s`;
        particles.appendChild(particle);
    }
});