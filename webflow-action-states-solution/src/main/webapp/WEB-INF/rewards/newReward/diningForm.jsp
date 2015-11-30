<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles-extras" prefix="tilesx" %>

<tilesx:useAttribute name="rewardButtonEvent"/>
<tilesx:useAttribute name="showOneClickReward" ignore="true"/>

<form:form id="diningForm" method="post" modelAttribute="diningForm">
	<fieldset> 
		<legend>
			<fmt:message key="enterDiningInformation.legend"/>
		</legend>
		<ul>
			<li>
				<label for="creditCardNumber">
					<fmt:message key="label.DiningForm.creditCard" />
				</label>
				<div class="control">
					<form:input path="creditCardNumber"/>
					<fmt:message var="errorMessage" key="error.invalidFormat.DiningForm.creditCardNumber"/>
					<form:errors path="creditCardNumber" cssClass="error" />
				</div>
			</li>
			<li>
				<label for="merchantNumber">
					<fmt:message key="label.DiningForm.merchantNumber" />
				</label>
				<div class="control">
					<form:select path="merchantNumber" items="${restaurants}" />
				</div>
			</li>
			<li>
				<label for="amount">
					<fmt:message key="label.DiningForm.amount" />
				</label>
				<div class="control">
					<form:input path="amount"/>
					<fmt:message var="errorMessage" key="diningForm.amount.typeMismatch" />
					<form:errors path="amount" cssClass="error" />
				</div>
			</li>
			<li>
				<label for="date">
					<fmt:message key="label.DiningForm.date" />
				</label>
				<div class="control">
					<form:input path="date"/>
					<fmt:message var="errorMessage" key="diningForm.date.typeMismatch" />
					<form:errors path="date" cssClass="error" />
				</div>
			</li>
			<c:if test="${not empty showOneClickReward}">
				<li>
						<label for="oneClickReward">
							<fmt:message key="label.DiningForm.oneClickReward" />
						</label>
						<div class="control">
							<form:checkbox path="oneClickReward"/>
						</div>
				</li>			
			</c:if>
		</ul>
		
		<button id="rewardButton" name="_eventId_<c:out value="${rewardButtonEvent}" default="reward"/>" type="submit">
			<fmt:message><tiles:insertAttribute name="rewardButtonText"/></fmt:message>
		</button>

	</fieldset>
</form:form>