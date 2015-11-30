<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<h1>
	<fmt:message key="accounts.show.title" />
</h1>

<ul class="controlLinks">
	<li><c:url var="newSearchUrl" value="search" /> <a
		href="${newSearchUrl}"> <fmt:message key="command.newSearch" />
	</a></li>
</ul>

<div id="entityDetails">
	<div id="accountDetails">
		<ul>
			<li><fmt:message key="label.Account.number" />:
				${account.number}

				<!--  Only editors can click Edit -->
				<security:authorize access="hasRole('ROLE_EDITOR')">
					<spring:url var="editUrl" value="{number}/edit">
						<spring:param name="number" value="${account.number}" />
					</spring:url>
				(<a id="editUrl" href="${editUrl}"><fmt:message
							key="command.edit" /></a>)
				</security:authorize></li>
			<li><fmt:message key="label.Account.name" />: ${account.name}</li>
			<li><fmt:message key="label.Account.dateOfBirth" />: <fmt:formatDate
					value="${account.dateOfBirth}" /></li>
			<li><fmt:message key="label.Account.email" />: ${account.email}
			</li>
			<li><fmt:message key="label.Account.receiveNewsletter" />: <fmt:message
					key="label.${account.receiveNewsletter}" /></li>
			<li><fmt:message key="label.Account.receiveMonthlyEmailUpdate" />:
				<fmt:message key="label.${account.receiveMonthlyEmailUpdate}" /></li>
		</ul>
	</div>

	<!-- Only viewers can see the Benefirciaries -->
	<security:authorize access="hasRole('ROLE_VIEWER')">
		<div id="beneficiaries">
			<h2>
				<fmt:message key="label.Account.beneficiaries" />
			</h2>
			<c:if test="${!empty account.beneficiaries}">
				<ul>
					<li>
						<table>
							<thead>
								<tr>
									<th><fmt:message key="label.Account.beneficiaries.name" />
									</th>
									<th><fmt:message
											key="label.Account.beneficiaries.allocationPercentage" /></th>
									<th><fmt:message key="label.Account.beneficiaries.savings" />
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
				<fmt:message key="accounts.show.label.noBeneficaries" />
			</c:if>
		</div>
	</security:authorize>

</div>