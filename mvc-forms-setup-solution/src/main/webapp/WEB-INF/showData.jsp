<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<div style="margin: 1em; padding:1em; background-color:#F0FFE0;">
    <h1>Data Submitted</h1>
    <p>Data received: </p>
   
    <p>If you can see all the data that you put in the form, you are done for now.</p>

	<c:choose>  
  		<c:when test="${not empty account}">  
    		<pre>${account}</pre>
    		<p>Notice that the beneficiaries have disappeared - this is because we have lost 
    		the original account from the database, it was in the <i>previous</i> request.
    		We will fix this in the next lab.</p>
  		</c:when>  
  		<c:otherwise> 
    		<pre>${accountSearchCriteria}</pre>
  		</c:otherwise> 
	</c:choose> 
 
</div>

<hr/>