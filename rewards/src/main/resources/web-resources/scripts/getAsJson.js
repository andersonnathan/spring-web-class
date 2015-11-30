/**
 * Send a synchronous HttpRequest and display resulting JSON in a popup window.
 * Intended for testing only.
 */
function getAsJson(url) {
	$.ajax({
	 	url : url,
	 	type : 'GET',
	 	datatype : 'json',
	 	async: false,
	 	success : function(data, textStatus, jqXHR) { 
	 		var jsonText = jqXHR.responseText.replace(/,/g, ",<br/>");
	 		jsonText = jsonText.replace(/\[/g,"<ul>[");
	 		jsonText = jsonText.replace(/\]/g,"]</ul>");

	 		var popup=window.open('','JSON Data','height=' + ($(window).height() / 1.6) +
	 				',width=' + ($(window).width() / 2.5) + ',alwaysRaised=yes,scrollbars=yes');
	 		popup.document.write("<html><body><p>" + jsonText + "</p></body></htnl>");
	 		popup.focus();
	 	},
	 	error : function() { alert('Failed!'); },
	 	beforeSend : function setHeader(xhr) { xhr.setRequestHeader('Accept', 'application/json'); }
	});
}