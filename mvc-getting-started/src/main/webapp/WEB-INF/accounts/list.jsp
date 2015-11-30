<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link type="text/css" rel="stylesheet" href="<c:url value="/styles/springsource.css"/>"/>

	<title>
		<fmt:message key="accounts.list.title" />
	</title>
</head>
<body>
<div id="page">
	<div id="header" class="clearfix">
	</div>
	<div id="content" class="clearfix">
		<div id="main">
			<h1>
				<fmt:message key="accounts.list.title" />
			</h1>
			<table>
				<thead>
					<tr>
						<th>
							<fmt:message key="label.Account.number"/>
						</th>
						<th>
							<fmt:message key="label.Account.name"/>
						</th>
					</tr>
				</thead>
				<c:forEach var="account" items="${accountList}">
					<tr>
						<!-- <td>
							<a href="accounts/${account.number}">${account.number}</a>
						</td>-->
		 				<td>
							<spring:url var="showUrl" value="accounts/{number}">
								<spring:param name="number" value="${account.number}" />
							</spring:url>
							<a href="${showUrl}">${account.number}</a>
						</td>
						<td>
							${account.name}
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<div id="nav">
			<ul class="controlLinks">
				<li>
					<a href="<c:url value="/admin/accounts"/>">
						<fmt:message key="navigate.accounts" />
					</a>
				</li>
			</ul>		
		</div>		
	</div>
	<div id="footer" class="clearfix">
		<p><fmt:message key="footer.message"/></p>
	</div>
</div>
</body>
</html>