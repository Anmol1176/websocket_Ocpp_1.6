$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});

	$("#send-table").click(function() {
		showTable();
	});

	$("#login").click(function() {
		loginConnect()
	});

	$("#send-user").click(function() {
		sendData();
	});

	$("#dis-connect").click(function() {
		loginDisConnect();
	});
	$("#send-edit").click(function() {
		userUpdate();
	});

});

function loginConnect() {

	let socket = new SockJS("/ws")

	stompClient = Stomp.over(socket)

	stompClient.connect({}, function(frame) {

		console.log("Connected : " + frame)
		//subscribe

	})
}


function loginDisConnect() {
	if (stompClient !== null) {
		stompClient.disconnect();
	}
	setLoginConnected(false);
	console.log("Disconnected");
}


function setLoginConnected(connected) {
	if (connected) {
		$("#conversation").show();
	}
	else {
		$("#conversation").hide();
	}
	$("#userDataTable").html("");
}


function sendData() {
	clearErrors();

	const formData = new FormData(form);

	let hasErrors = false;

	const username = formData.get('username');
	if (!isValidUsername(username)) {
		displayError('username', 'Username field are required !');
		hasErrors = true;
	}

	// Validate ID Tag (only allow alphanumeric characters and underscores)
	const idtag = formData.get('idtag');
	if (!isValidIdTag(idtag)) {
		displayError('idtag', 'ID Tag field are required !');
		hasErrors = true;
	}

	// Validate Expiry Date (add your custom expiry date validation logic here)
	const expirydate = formData.get('expirydate');
	if (!isValidExpiryDate(expirydate)) {
		displayError('expirydate', 'Expiry Date field are required !');
		hasErrors = true;
	}

	// Validate Parent ID Tag (only allow alphanumeric characters and underscores)
	const parentIdTag = formData.get('parentIdTag');
	if (!isValidIdTag(parentIdTag)) {
		displayError('parentIdTag', 'Parent ID Tag field are required !');
		hasErrors = true;
	}

	// Validate Status (add your custom status validation logic here)
	const status = formData.get('status');
	if (!isValidStatus(status)) {
		displayError('status', 'Status field are required !');
		hasErrors = true;
	}

	if (!hasErrors) {
		// Convert form data to JSON format
		const user = {
			userId: formData.get('userId'),
			username: formData.get('username'),
			idtag: formData.get('idtag'),
			expiryDate: formData.get('expirydate'),
			parentIdTag: formData.get('parentIdTag'),
			status: formData.get('status'),

		};
		stompClient.send("/app/message", {}, JSON.stringify(user));
		stompClient.subscribe('/topic/return-to', function(response) {
			var returnMessage = JSON.parse(response.body);
			var messageDisplay = document.getElementById('messageDisplay');
			var messageShow = document.getElementById('messageShow');
			if (returnMessage.error) {
				// Display the error message on the form
				messageDisplay.textContent = returnMessage.error;
			} else {
				// Display the success message on the form
				messageShow.textContent = returnMessage.message;
				// Reset the form after successful registration
				document.getElementById('userForm').reset();
			}


			if (returnMessage.error) {
				// Display the error message on the form
				//messageDisplay.textContent = returnMessage.error;
				messageShow.textContent = "Opps! idTag already exist!"
			} else {
				// Display the success message on the form
				messageShow.textContent = returnMessage.message;
				// Reset the form after successful registration
				document.getElementById('userForm').reset();
			}

			form.reset();
		})
		return false;
	}
}

function isValidUsername(username) {
	return username;
}

function isUnique(idtag) {
	return idtag;
}

function isValidIdTag(idtag) {
	const idtagPattern = /^\w+$/;
	return idtagPattern.test(idtag);
}

function isValidExpiryDate(expirydate) {
	return expirydate;
}

function isValidStatus(status) {
	return status.trim() !== '';
}

function displayError(fieldName, errorMessage, color = 'red', fontSize = '14px') {
	// Display validation error for the specified field
	const errorDiv = document.getElementById(`${fieldName}Error`);
	if (errorDiv) {
		errorDiv.innerHTML = errorMessage;
		errorDiv.style.color = color;
		errorDiv.style.fontSize = fontSize;
	}
}

function clearErrors() {
	// Clear previous error messages on the form
	const errorDivs = form.querySelectorAll('.error');
	errorDivs.forEach((div) => {
		div.innerHTML = '';
	});
}


/*
function showData(user) {
	$("#userDataTable").prepend(`<tr>
								 <td>${user.username}<td>
								 <td>${user.idtag}<td>
								 <td>${user.expiryDate}<td>
								 <td>${user.parentIdTag}<td>
								 <td>${user.status}</td>
								 </tr>`
	)
}
*/



function showData() {
	$("#userDataTable").prepend("Data Successfully Insertion !")
}

function showTable() {

	let socket = new SockJS("/ws")

	stompClient = Stomp.over(socket)

	stompClient.connect({}, function(frame) {

		console.log("Connected : " + frame)

		stompClient.send('/app/getData', {}, '');

		stompClient.subscribe('/topic/userData', function(message) {
			const allUser = JSON.parse(message.body);
			updateTable(allUser);
		})
	})
}

function updateTable(allUser) {
	const myButton = document.getElementById('send-table');
	const tableBody = document.getElementById("userDataBody");
	myButton.innerHTML = 'Refresh';
	myButton.style.backgroundColor = 'green';
	myButton.classList.add('darkgreen');
	tableBody.innerHTML = "";

	allUser.forEach((user) => {
		const row = document.createElement("tr");
		row.innerHTML = `
            
            <td>${user.username}</td>
            <td>${user.idtag}</td>
            <td>${user.expiryDate}</td>
            <td>${user.parentIdTag}</td>
            <td>${user.status}</td>
            <td>
                <button onclick="openEditForm(${user.id},'${user.username}','${user.idtag}','${user.expiryDate}','${user.parentIdTag}','${user.status}')" style="border: none; color:blue;" ><span id="updateButton" style="color:blue;">&#9998;</span></button>
                <button style="border: none;" onclick="deleteUser(${user.id})">&#128465;</button>
            </td>
            
          `;
		tableBody.appendChild(row);
	});
}

function openEditForm(userId, userName, idTag, expiryDate, parentidTag, Status) {
	document.getElementById('userId').value = userId;
	document.getElementById('userName').value = userName;

	document.getElementById('idTag').value = idTag;
	document.getElementById('expiryDate').value = expiryDate;
	document.getElementById('parentidTag').value = parentidTag;
	document.getElementById('Status').value = Status;

	$("#updateForm").show();
	$("#userForm").hide();

	let socket = new SockJS("/ws")

	stompClient = Stomp.over(socket)

	stompClient.connect({}, function(frame) {

		console.log("Connected : " + frame)

		/*		stompClient.subscribe('/topic/updatedUserData', function(message) {
					const updatedUser = JSON.parse(message.body);
					// You can handle the updated data if needed
					console.log("Updated User Data:", updatedUser);
				})*/
	})
}


function deleteUser(userId) {
	Swal.fire({
		title: 'Confirm Delete',
		text: 'Are you sure you want to delete this user?',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonText: 'Yes, delete it!',
		cancelButtonText: 'Cancel',
		dangerMode: true // Adds a red-colored "Delete" button
	}).then((result) => {
		if (result.isConfirmed) {
			// User confirmed the delete, send the delete message to the backend WebSocket controller
			stompClient.send('/app/deleteUser', {}, userId.toString());

			// Show a success message using Swal
			Swal.fire('Deleted!', 'The user has been deleted.', 'success');
		} else if (result.dismiss === Swal.DismissReason.cancel) {
			Swal.fire('Cancelled', 'You clicked "No".', 'error');
		}
	});
}

function updateUser() {
	const form = document.getElementById('updateForm');
	const userId = document.getElementById('userId').value;
	const userName = document.getElementById('userName').value;
	const idTag = document.getElementById('idTag').value;
	const expiryDate = document.getElementById('expiryDate').value;
	const parentidTag = document.getElementById('parentidTag').value;
	const Status = document.getElementById('Status').value;

	const updateUser = {
		userId: userId,
		username: userName,
		idtag: idTag,
		expiryDate: expiryDate,
		parentIdTag: parentidTag,
		status: Status
	};

	// Show the SweetAlert confirm dialog
	Swal.fire({
		title: 'Confirm Update',
		text: 'Are you sure you want to update the user?',
		icon: 'question',
		showCancelButton: true,
		confirmButtonText: 'Yes',
		cancelButtonText: 'No'

	}).then((result) => {
		if (result.isConfirmed) {
			// User confirmed the update, send the data to the server via WebSocket
			stompClient.send('/app/update/' + userId, {}, JSON.stringify(updateUser));
			// Reset the form after sending data
			form.reset();
			// Show a success message using Swal
			Swal.fire('Updated!', 'User data has been updated.', 'success');

		} else if (result.dismiss === Swal.DismissReason.cancel) {
			Swal.fire('Cancelled', 'You clicked "No".', 'error');
		}
	});
}

