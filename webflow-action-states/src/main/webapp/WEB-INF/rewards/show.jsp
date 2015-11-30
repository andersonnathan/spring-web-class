<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<h1>
	<fmt:message key="rewards.show.title" />
</h1>

<div id="entityDetails">
	<ul>
		<li>
			<label><fmt:message key="label.Reward.confirmationNumber"/></label> ${reward.confirmationNumber}
		</li>
		<li>
			<label><fmt:message key="label.Reward.accountNumber"/></label> ${reward.accountNumber}
		</li>
		<li>
			<label><fmt:message key="label.Reward.merchantNumber"/></label> ${reward.merchantNumber}
		</li>
		<li>
			<label><fmt:message key="label.Reward.amount"/></label> ${reward.amount}
		</li>
		<li>
			<label><fmt:message key="label.Reward.date"/></label> <fmt:formatDate value="${reward.date}" />
		</li>
	</ul>
</div>