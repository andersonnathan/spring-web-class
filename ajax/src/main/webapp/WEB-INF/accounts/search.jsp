<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1>
	<fmt:message key="accounts.newSearch.title" />
</h1>

<c:url value="/accounts" var="accountsUrl"/>
<form:form id="accountSearchForm" action="${accountsUrl}" modelAttribute="accountSearchCriteria" method="get">
	<fieldset>
		<legend>
			<fmt:message key="label.AccountSearchCriteria"/>
		</legend>
		<ul>
			<li>
				<label>
					<fmt:message key="label.AccountSearchCriteria.searchString"/>
				</label>
				<div class="control">
					<form:input path="searchString"/>
					<form:errors cssClass="error" path="searchString"/>
				</div>
			</li>
			<li>
				<label>
					<fmt:message key="label.AccountSearchCriteria.maximumResults"/>
				</label>
				<div class="control">
					<form:select path="maximumResults" style="width:8em;">
						<form:option label="5" value="5"/>
						<form:option label="10" value="10"/>
						<form:option label="15" value="15"/>
					</form:select>
				</div>
			</li>
			<li>
				<button id="searchButton" type="submit" style="width:8em; color:blue;">
					<fmt:message key="command.search"/>
				</button>
				Normal search, refreshes entire page
			</li>
			<li>
				<button id="jsonSearchButton" type="submit" style="width:8em; color:green;">
					JSON-<fmt:message key="command.search"/>
				</button>
				Performs AJAX request and reveals hidden &lt;div&gt; holding results
			</li>
		</ul>
	</fieldset>
</form:form>

<!--  This is the hidden div where the results will go -->
<div id="accountsListingFragment">
    <h2>JSON Results</h2>
	<table>
		<thead>
			<tr>
				<td><fmt:message key="label.Account.number"/></td>
				<td><fmt:message key="label.Account.name"/></td>
			</tr>
		</thead>
		<tbody id="resultTable">
		</tbody>
	</table>
</div>

<!--  Used to display any AJAX error messages -->
<div id="error" style="color:red; margin: 1em">error goes here ...</div>
<div id="dialog" style="text-align:left; background: #FFEEEE" title="Error">error details go here ...</div>

<script type="text/javascript">
$(function() {
	$("#error").hide();
	$("#dialog").hide();

	// TODO-01: Deploy this application to the server.
	//          Access the home page at http://localhost:8080/ajax.
	//          Remove any previously deployed applications from your server to allow for faster startup.
	//          Verify it works using the blue Search button
	//	   Then ...
	
	// TODO-02: Use jQuery to hide the accountsListingFragment div tag (and its child tags)
	//            Return to the account search page and check the Results table has disappeared
	//    NOTE: Remember to refresh the search page EVERY TIME you modify this file.

	// TODO-03: Associate the handler function 'processAjaxSubmit' with the click event of the JSON
	//             Search button using jQuery on() function.
	//          Reload the search page and click the green JSON-Search button - an error message should
	//            appear and focus jump to the Search String input box because no data has been entered.  If you enter some text and
	//            submit, the search will not yet be performed. See the next step ...

});

function processAjaxSubmit() {
	$("#error").hide();

	// TODO-04: Obtain the searchString and maximumResults values from the form input fields and
	//          assign them to the two variables. 
	<%--
	  //  Note that Spring's form:input tags generate "id" and "name" attributes based on
	  //  the tag's "path" attribute.  For example <form:input path="searchString"/> generates
	  //  the following HTML in the rendered page:
	  //       <input type-"text" id="searchString" name= "searchString" ... />
	--%>

	var searchString = null;
	var maximumResults = null;

	if (searchString.length == 0) {
		$("#searchString").focus();   // Force focus to missing field
	} else {

		// TODO-05: Modify the 'params' JSON object below.  Establish properties matching
		//          the fields of the AccountSearchCriteria class. Set these property values based 
		//          on the form input field values you saved above:
		var params = { }
		
		// TODO-06: Now we need to invoke a RESTful method to get the accounts.  Find the TO DO
		//          in AccountSearchController.java.  Modify the searchURL to invoke that method.
		//    HINT: ${accountsUrl} is defined at the top of this file and is a URL that maps
		//          to the AccountSearchController.
		var searchURL = "${accountsUrl}";  // Modify this URL

		// TODO-07: Uncomment and modify the line below to invoke JQuery's getJSON function.
		//          Pass searchURL (defined above) as the URL.  For params, pass the JSON object
		//          built in a previous step.  Specify the displayResponse function (defined 
		//          below) as the callback handler.
		//    NOTE: getJSON sets the Accept header, you shouldn't need the .json suffix in the URL
		// $.getJSON(...).error(displayError);
	}

	return false;  // Don't submit form normally
}

function displayResults(results) {
	if (results.length == 0) {
		alert("No results for search");
	} else {
		$("#resultTable").empty();
		for (var i=0;i<results.length;i++) {

		   // TODO-08: Optional Bonus 1
		   //          Modify the accounts display list to include a link with an onclick event that calls 
		   //          processAjaxAccountDetails, passing in the account number.  The RESTful method to
		   //          fetch account-details already exists on AccountsController.
		   //    Hint: Modify the <a> element and define its onclick event.
		   //          The account number links should now popup an account details dialog.
		   $("#resultTable").append('<tr><td><a href="#">' +
				   results[i].number +
				   '</a></td><td>'
				   + results[i].name +
				   '</td></tr>');	
		}

		$("#accountsListingFragment").fadeIn('fast');
	}
}

// TODO-09: View the AJAX requests using Firebug - refer to the lab-documentation for details.

function processAjaxAccountDetails(accountNum) {
	// Display a simple popup - quick but easy
	$.getJSON(accountNum, function(results) {
		var account = results;
		var s = "";

		if (account == null)
			s = "Account not found.";
		else {
			var s = "=== Account Details ===\n\n";
			s += "Account Number: " + account.number + "\n";
			s += "Account Name: " + account.name + "\n";
			s += "Date of birth: " + new Date(account.dateOfBirth).toString().substring(0, 15) + "\n";
			s += "Receive newsletter? " + account.receiveNewsletter + "\n";
		}

		alert(s);
	});
}

// Display text of AJAX error message using a hidden div
function displayError(xhr, status, msg) {
	// Display an error in the 'error' <div> above and show it.
	var text = xhr.responseText;
	var emsg = "<p><b>AJAX Failure: " + xhr.status + " " + msg
			+ "</b><br/>";

	$("#error").html(emsg);
	$("#error").show();

	// If HTML returned, popup a JQuery UI dialog to help debuging
	if (xhr.getResponseHeader('content-type').indexOf("text/html") != -1) {
		emsg += "</b><p>Looks like you fetched HTML by mistake?</p><p>"
				+ "Server response was:<br/>";
		var p = text.indexOf("<body>");
		text = (p != -1) ? text.substring(p) : text;
		emsg += text;
	}
	else
	{
		emsg += "<p>" + text.replace(/\n/g, " <br/>").replace(/:/g, " :<br/>") + "</p>";	
	}

	$("#dialog").html(emsg);
	$("#dialog").dialog( { width: "80%", minWidth: "80%", minHeight: 100, maxHeight: 300 } );
	$("#dialog").dialog( "open" );
}
</script>
