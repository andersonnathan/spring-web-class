<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<h1>
	<fmt:message key="accounts.show.title" />
</h1>

<ul class="controlLinks">
	<li>
		<c:url var="newSearchUrl" value="search" />
		<a href="${newSearchUrl}">
			<fmt:message key="command.newSearch" />
		</a>
	</li>
</ul>

<div id="entityDetails">
	<div id="accountDetails">
		<ul>
			<li>
				<fmt:message key="label.Account.number" />: ${account.number}
	<%-- TODO-09: Using 'vince', go to the 'account details' page and then click on 'Edit accounts'.
					As you can see, access to this page is denied because 'vince' does not have the EDITOR role.
					It would be more elegant to hide this link from 'vince' and only show it to editors.
				- Using the 'security' tag library, hide the 'Edit' link below unless a
	              	user has permission to access that page. 
	            - Try logging in as a user with and without the editor role and verify that you see 
	              	the correct behavior.
	--%>

				<spring:url var="editUrl" value="{number}/edit">
					<spring:param name="number" value="${account.number}" />
				</spring:url>
				(<a id="editUrl" href="${editUrl}"><fmt:message key="command.edit" /></a>)

			</li>
			<li>
				<fmt:message key="label.Account.name" />: ${account.name}
			</li>
			<li>
				<fmt:message key="label.Account.dateOfBirth"/>: <fmt:formatDate value="${account.dateOfBirth}" />
			</li>
			<li>
				<fmt:message key="label.Account.email" />: ${account.email}
			</li>
			<li>
				<fmt:message key="label.Account.receiveNewsletter" />: 
				<fmt:message key="label.${account.receiveNewsletter}" />
			</li>
			<li>
				<fmt:message key="label.Account.receiveMonthlyEmailUpdate" />: 
				<fmt:message key="label.${account.receiveMonthlyEmailUpdate}" />
			</li>
		</ul>
	</div>
	
	<%-- TODO-10: Only viewers should be allowed to view beneficiaries
			      information.  Hide the beneficiaries div below from
			      all users who do NOT have the "VIEWER" role.
	 --%>
	
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
								<th>
									<fmt:message key="label.Account.beneficiaries.name" />
								</th>
								<th>
									<fmt:message key="label.Account.beneficiaries.allocationPercentage" />
								</th>
								<th>
									<fmt:message key="label.Account.beneficiaries.savings" />
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
</div>