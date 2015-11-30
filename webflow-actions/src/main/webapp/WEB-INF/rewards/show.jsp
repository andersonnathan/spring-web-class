<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<h1>
	<fmt:message key="rewards.show.title" />
</h1>

<!-- TODO-11: Remove hard-coded values and modify to use dynamic data. Look in
              RewardsController to see what model data is available. -->

<div id="entityDetails">
	<ul>
		<li><label><fmt:message
					key="label.Reward.confirmationNumber" /></label> 1</li>
		<li><label><fmt:message key="label.Reward.accountNumber" /></label>
			123456789</li>
		<li><label><fmt:message key="label.Reward.merchantNumber" /></label>
			0987654321</li>
		<li><label><fmt:message key="label.Reward.amount" /></label>
			$23.00</li>
		<li><label><fmt:message key="label.Reward.date" /></label>
			2008-10-05</li>
	</ul>
</div>