function toggleSearch() {
	var form = document.getElementById("search-form");
	var button = document.getElementById("search-button");
	var cancelButton = document.getElementById("cancel-search-button");
	var footerRow = document.getElementById("footer-row");

	if (form.style.display === "none") {
		form.style.display = "block";
		button.style.display = "none";
		cancelButton.style.display = "block";
		footerRow.style.display = "none";
	} else {
		form.style.display = "none";
		button.style.display = "block";
		cancelButton.style.display = "none";
		footerRow.style.display = "table-row";
	}


}

function handleSearchSubmit(event) {
	event.preventDefault(); // Prevent the default form submission behavior

	var footerRow = document.getElementById("footer-row");
	var cancelButton = document.getElementById("cancel-search-button");

	// Hide the footer row and show the cancel button after submitting the search form
	footerRow.style.display = "none";
	cancelButton.style.display = "block";

	// Fetch and update the table content
	var form = document.getElementById("search-form");
	var formData = new FormData(form);

	// Replace the URL '/search' with the actual URL that processes the search form on your server
	fetch('/search', {
		method: 'POST',
		body: formData
	})
		.then(response => response.text())
		.then(data => {
			// Create a temporary DOM element to hold the fetched HTML content
			var tempElement = document.createElement('div');
			tempElement.innerHTML = data;

			// Extract the tbody content from the fetched HTML
			var fetchedTBody = tempElement.querySelector('table tbody');

			// Replace the table content with the new data
			var tableBody = document.querySelector("table tbody");
			tableBody.innerHTML = fetchedTBody.innerHTML;
		})
		.catch(error => {
			console.error('Error fetching search results:', error);
		});

	return false;
}

function cancelSearch() {
	window.location.href = "/expenselist";
}


function toggleEdit(inputId, buttonId) {
	var input = document.getElementById(inputId);
	var editButton = document.getElementById(buttonId);

	if (input.readOnly) {
		input.readOnly = false;
		editButton.textContent = "Cancel";
	} else {
		input.readOnly = true;
		editButton.textContent = "Edit";
	}
}

