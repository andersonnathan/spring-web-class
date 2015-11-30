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
	$("#accountsListingFragment").hide();
	$("#error").hide();
	$("#dialog").hide();
	$("#jsonSearchButton").on('click', processAjaxSubmit);
});

function processAjaxSubmit(event) {
	$("#error").hide();
	$("#dialog").hide();
	
	var searchString = $("#searchString").val();
	var maxResults = $("#maximumResults").val();

	if (searchString.length == 0) {
		$("#searchString").focus();   // Force focus to missing field
	} else {
		var accountsURL = "${accountsUrl}/search";

		// Setup query (search) parameters
		var params = {
			searchString : searchString,
			maximumResults : maxResults
		}
		
		// Perform AJAX request ...
		$.getJSON(accountsURL, params, displayResults)
			.fail(displayError);
	}
	return false;
}

function displayResults(results) {
	if (results.length == 0) {
		alert("No results for search");
	} else {
		$("#resultTable").empty();
		for (var i=0;i<results.length;i++) {
		   // Original code: $("#resultTable").append('<tr><td>' + results[i].number + '</td><td>' + results[i].name + '</td></tr>');	
		   $("#resultTable").append('<tr><td><a href="#" onclick="processAjaxAccountDetails(' +
				   results[i].number + ')" >' +
				   results[i].number +
				   '</a></td><td>' +
				   results[i].name +
				   '</td></tr>');	
		}

		$("#accountsListingFragment").fadeIn('fast');
	}
}

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

// Dsiplay text of AJAX error message using a hidden div
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
