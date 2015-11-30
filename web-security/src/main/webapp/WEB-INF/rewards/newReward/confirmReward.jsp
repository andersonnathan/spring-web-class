<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<form method="post" id="reviewRewardFragment">
	<fieldset>
		<legend><fmt:message key="label.Reward" /></legend>
		<ul>
			<li><label><fmt:message key="label.Account.number" /></label> ${accountContribution.accountNumber}</li>
			<li><label><fmt:message key="label.Reward.amount" /></label> ${accountContribution.amount}</li>
			<c:if test="${! empty accountContribution.distributions}">
				<li>
					<label><fmt:message key="label.Account.beneficiaries"/></label>
					<table>
						<thead>
							<tr>
								<td><fmt:message key="label.Account.beneficiaries.name"/></td>
								<td><fmt:message key="label.Account.beneficiaries.amount"/></td>
								<td><fmt:message key="label.Account.beneficiaries.allocationPercentage"/></td>
								<td><fmt:message key="label.Account.beneficiaries.savings"/></td>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${accountContribution.distributions}" var="distribution">
								<tr>
									<td>${distribution.beneficiary}</td>
									<td>${distribution.amount}</td>
									<td>${distribution.percentage}</td>
									<td>${distribution.totalSavings}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</li>
			</c:if>
		</ul>
		<button type="submit" name="_eventId_confirm"><fmt:message key="command.confirm"/></button>
	</fieldset>
</form>
