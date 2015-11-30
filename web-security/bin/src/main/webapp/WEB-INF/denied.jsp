<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<h1><fmt:message key="denied.title"/></h1>

<p><fmt:message key="denied.caption"/></p>

<!-- TODO-07b: Logout link is for convenience when playing with roles & access.
              Remove this entire <ul> after you add a logout option to standard.jsp. -->
<ul class="clearfix">
	<li>
	    <!--  Copy this line to standard.jsp -->
	    <p><a href="<c:url value="/j_spring_security_logout"/>"><fmt:message key="navigate.logout"/></a></p>
	</li>
</ul>


