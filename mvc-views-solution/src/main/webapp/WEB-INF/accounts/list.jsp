<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<h1>
	<fmt:message key="accounts.list.title" />
</h1>

<div>
	<a href="accounts.xls"><fmt:message key="accounts.list.command.asExcel"/></a>
	&#160;
	<a href="accounts.json"><fmt:message key="accounts.list.command.asJson"/></a>
</div>

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
				<spring:url var="showUrl" value="/accounts/{number}">
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