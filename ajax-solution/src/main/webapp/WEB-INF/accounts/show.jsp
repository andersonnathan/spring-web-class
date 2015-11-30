<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<ul class="controlLinks">
	<li>
		<c:url var="newSearchUrl" value="search" />
		<a href="${newSearchUrl}">
			<fmt:message key="command.newSearch"/>
		</a>
	</li>
</ul>

<div id="entityDetails">
	<div id="accountDetails">
		<ul>
			<li>
				<fmt:message key="label.Account.number"/>: ${account.number}
				<spring:url var="editUrl" value="{number}/edit">
					<spring:param name="number" value="${account.number}" />
				</spring:url>
				(<a id="editUrl" href="${editUrl}"><fmt:message key="command.edit"/></a>)
				<script type="text/javascript">
					var ajaxHandler = new richweb.AjaxHandler();
					dojo.connect(dojo.byId('editUrl'), 'onclick', 
						function(event) { 
							ajaxHandler.getResource(dojo.byId('editUrl').href, 'main', 'entityDetails');
							event.preventDefault();
						}
					);
				</script>
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
		<h5>
			<fmt:message key="label.Account.beneficiaries"/>
		</h5>
		<c:if test="${! empty account.beneficiaries}">
			<ul>
				<li>
					<table>
						<thead>
							<tr>
								<td><fmt:message key="label.Account.beneficiaries.name"/></td>
								<td><fmt:message key="label.Account.beneficiaries.allocationPercentage"/></td>
								<td><fmt:message key="label.Account.beneficiaries.savings"/></td>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${account.beneficiaries}" var="beneficiary">
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