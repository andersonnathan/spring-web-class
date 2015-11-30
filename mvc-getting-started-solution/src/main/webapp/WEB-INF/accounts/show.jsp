<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link type="text/css" rel="stylesheet" href="<c:url value="/styles/springsource.css"/>"/>
	<title>
		<fmt:message key="accounts.show.title"/>
	</title>
</head>
<body>
<div id="page">
	<div id="header" class="clearfix">
	</div>
	<div id="content" class="clearfix">
		<div id="main">
			<h1>
				<fmt:message key="accounts.show.title"/>
			</h1>
			<div id="entityDetails">
				<div id="accountDetails">
					<ul>
						<li>
							<fmt:message key="label.Account.number"/>: ${account.number}
						</li>
						<li>
							<fmt:message key="label.Account.name"/>: ${account.name}
						</li>
						<li>
							<fmt:message key="label.Account.dateOfBirth"/>: <fmt:formatDate value="${account.dateOfBirth}"/>
						</li>
						<li>
							<fmt:message key="label.Account.email"/>: ${account.email}
						</li>
						<li>
							<fmt:message key="label.Account.receiveNewsletter"/>: 
							<fmt:message key="label.${account.receiveNewsletter}"/>
						</li>
						<li>
							<fmt:message key="label.Account.receiveMonthlyEmailUpdate"/>: 
							<fmt:message key="label.${account.receiveMonthlyEmailUpdate}"/>
						</li>
					</ul>
				</div>
				<div id="beneficiaries">
					<h2>
						<fmt:message key="label.Account.beneficiaries"/>
					</h2>
					<c:if test="${!empty account.beneficiaries}">
						<ul>
							<li>
								<table>
									<thead>
										<tr>
											<th>
												<fmt:message key="label.Account.beneficiaries.name"/>
											</th>
											<th>
												<fmt:message key="label.Account.beneficiaries.allocationPercentage"/>
											</th>
											<th>
												<fmt:message key="label.Account.beneficiaries.savings"/>
											</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="beneficiary" items="${account.beneficiaries}">
											<tr>
												<td>${beneficiary.name}</td>
												<td>${beneficiary.allocationPercentage}</td>
												<td>${beneficiary.savings}</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</li>
						</ul>
					</c:if>
					<c:if test="${empty account.beneficiaries}">
						<fmt:message key="accounts.show.label.noBeneficaries"/>
					</c:if>
				</div>
			</div>
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