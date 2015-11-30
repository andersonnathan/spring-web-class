<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<h1>
	<fmt:message key="accounts.search.title" />
</h1>

<c:if test="${not empty accountSearchCriteria.searchString}">
	<ul class="controlLinks">
		<li>
			<c:url var="changeSearchUrl" value="accounts/search">
				<c:param name="searchString" value="${accountSearchCriteria.searchString}" />
				<c:param name="maximumResults" value="${accountSearchCriteria.maximumResults}" />
			</c:url>
			<a id="changeSearchUrl" href="${changeSearchUrl}">
				<fmt:message key="command.changeSearch" />
			</a>
		</li>
	</ul>
</c:if>

<div id="accountsListingFragment">
	<table>
		<thead>
			<tr>
				<td>
					<fmt:message key="label.Account.number" />
				</td>
				<td>
					<fmt:message key="label.Account.name" />
				</td>
			</tr>
		</thead>
		<c:forEach var="account" items="${accountList}">
			<tr>
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
	
	<div class="controlLinks">
		&nbsp; &nbsp;
		<c:if test="${accountSearchCriteria.page > 0}">
			<span class="button">
				<c:url var="previousUrl" value="accounts">
					<c:param name="searchString" value="${accountSearchCriteria.searchString}" />
					<c:param name="maximumResults" value="${accountSearchCriteria.maximumResults}" />
					<c:param name="page" value="${accountSearchCriteria.page - 1}" />
				</c:url>
				<a id="previousUrl" href="${previousUrl}">
					&lt;&lt; <fmt:message key="command.previous" />
				</a>
			</span>
		</c:if>
		&nbsp; &nbsp;
		<c:if test="${not empty accountList && fn:length(accountList) eq accountSearchCriteria.maximumResults}">
			<span class="button">
				<c:url var="nextUrl" value="accounts">
					<c:param name="searchString" value="${accountSearchCriteria.searchString}" />
					<c:param name="maximumResults" value="${accountSearchCriteria.maximumResults}" />
					<c:param name="page" value="${accountSearchCriteria.page + 1}" />
				</c:url>
				<a id="nextUrl" href="${nextUrl}">
					<fmt:message key="command.next" /> &gt;&gt;
				</a>
			</span>
		</c:if>
	</div>

</div>