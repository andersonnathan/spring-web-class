<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1>
	<fmt:message key="password.form.title" />
</h1>

<c:url value="/password/hash" var="passwordUrl"/>
<form:form action="${passwordUrl}" modelAttribute="passwordForm" method="get">
	<fieldset>
		<legend>
			<fmt:message key="label.passwordForm.legend" />
		</legend>
		<ul>
			<li>
				<label>
					<fmt:message key="label.passwordForm.password" />
				</label>
				<div class="control">
					<form:input path="password" />
					<fmt:message key="error.passwordForm.password" var="errorMessage" />
					<form:errors cssClass="error" path="password" />
				</div>
			</li>
			<li>
				<label>
					<fmt:message key="label.passwordForm.algorithm" />
				</label>
				<div class="control">
					<form:select path="algorithm">
						<form:option label="md5" value="md5"/>
						<form:option label="sha-1" value="sha-1"/>
						<form:option label="sha-256" value="sha-256"/>
					</form:select>
				</div>
			</li>
			<li>
				<label>
					<fmt:message key="label.passwordForm.salt" />
				</label>
				<div class="control">
					<form:input path="salt" value="MySalt" />
					<fmt:message key="error.passwordForm.salt" var="errorMessage" />
					<form:errors cssClass="error" path="salt" />
				</div>
			</li>
			<li>
				<label>
					<fmt:message key="label.passwordForm.hash" />
				</label><br/>
				<div style="text-align:center; padding-top:6px; padding-bottom:6px; color: blue; font-size: 11pt;">
					${passwordForm.hash}
				</div>
			</li>
		</ul>
		<button id="generateButton" type="submit">
			<fmt:message key="command.generate"/>
		</button>
	</fieldset>
</form:form>