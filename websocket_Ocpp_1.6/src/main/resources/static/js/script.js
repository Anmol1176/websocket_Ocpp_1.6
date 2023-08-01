function handleButtonClick() {
  // Your code to perform actions when the button is clicked goes here.

  // Show the success message
  const messageShow = document.getElementById('messageShow');
  //messageShow.style.display = 'block';

  // Reset the message after 2 seconds (you can adjust the time as needed)
  setTimeout(() => {
    messageShow.style.display = 'none';
  }, 2000);
}

function startCharging() {
	// Get the input element
	const inputElement = document.getElementById("message-value");

	// Prompt the user for input and store it in a variable
	const userInput = [2, "jgdkjgshkjhkjdshkhak", "RemoteStartTransaction", { "connectorId": 2, "idtag": "companyname", "chargingProfile": null }];

	// Convert the object to a JSON string
	const jsonString = JSON.stringify(userInput);

	// Update the input element's value with the user input
	inputElement.value = jsonString;
}

function stopCharging() {
	// Get the input element
	const inputElement = document.getElementById("message-value");

	// Prompt the user for input and store it in a variable
	const userInput = [2,"15980d2c-7b01-4fe3-8d2e-e31dd8f1d356","RemoteStopTransaction",{"transactionId":123456789}];


	// Convert the object to a JSON string
	const jsonString = JSON.stringify(userInput);

	// Update the input element's value with the user input
	inputElement.value = jsonString;
}
