<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${not empty param.login_error}">
	<div id="errors" class="error" style="font-size: large; width:100%">
		<p><fmt:message key="login.unsuccessful"/>: ${SPRING_SECURITY_LAST_EXCEPTION.message}</p>
	</div>	
</c:if>

<br/><br/><br/><br/>

<form action="<c:url value='/j_spring_security_check'/>" method="post">
	<fieldset>
		<legend style="font-size: medium;"><fmt:message key="login.hint"/></legend>
		<ul>
			<li>
				<label for="j_username"><fmt:message key="login.user"/></label>
				<div class="control">
					<input type='text' id='j_username' name='j_username' value='<c:out value="${user}"/>'/>
				</div>
			</li>
			<li>
				<label for="j_password"><fmt:message key="login.password"/></label>
				<div class="control">
					<input type='password' id='j_password' name='j_password'/>
				</div>
			</li>
			<li class="confirm">
				<label>
					<input type="checkbox" name="_spring_security_remember_me"/>
					<fmt:message key="login.remember"/>
				</label>
			</li>
		</ul>
		<button type="submit"><fmt:message key="login.submit"/></button>
	</fieldset>
</form>
