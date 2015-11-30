<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>
		<fmt:message>
			<tiles:insertAttribute name="title"/>
		</fmt:message>
	</title>
	<spring:theme var="cssRichweb" code="css.richweb" />
	<style type="text/css" media="screen">
        @import url("<c:url value="/styles/forms.css" />");
        @import url("<c:url value="/styles/entity.css" />");
        @import url("<c:url value="/styles/tables.css" />");
        @import url("<c:url value="${cssRichweb}" />");
    </style>
</head>
<body class="tundra">
	<div id="page">
		<div id="header" class="clearfix">
			<div id="branding">
				<spring:theme var="bannerImage" code="banner.image"/>
				<img src="<c:url value="${bannerImage}"/>" />
			</div>
			<div id="personalization">
				<c:if test="${requestContext.locale.language eq 'en'}">
					<c:url var="localeUrl" value="/">
						<c:param name="locale" value="fr"/>
					</c:url>
					<a href="${localeUrl}">
						<fmt:message key="locale.fr" />
					</a>
				</c:if>
				<c:if test="${requestContext.locale.language eq 'fr'}">
					<c:url var="localeUrl" value="/">
						<c:param name="locale" value="en"/>
					</c:url>
					<a href="${localeUrl}">
						<fmt:message key="locale.en" />
					</a>
				</c:if> |
				<c:if test="${requestContext.theme.name eq 'green'}">
					<c:url var="themeUrl" value="/">
						<c:param name="theme" value="blue"/>
					</c:url>
					<a href="${themeUrl}">
						<fmt:message key="theme.Blue" />
					</a>
				</c:if>
				<c:if test="${requestContext.theme.name eq 'blue'}">
					<c:url var="themeUrl" value="/">
						<c:param name="theme" value="green"/>
					</c:url>
					<a href="${themeUrl}">
						<fmt:message key="theme.Green" />
					</a>
				</c:if>						
			</div>			
		</div>
		<div id="content" class="clearfix">
			<div id="main">
				<tiles:insertAttribute name="main" />
			</div>
			<div id="nav">
				<tiles:importAttribute name="navigationTab" />
				<ul class="clearfix">
					<li>
						<c:if test="${navigationTab eq 'home'}">
							<strong>
								<a href="<c:url value="/"/>">
									<fmt:message key="navigate.home"/>
								</a>
							</strong>
						</c:if>
						<c:if test="${navigationTab != 'home'}">
							<a href="<c:url value="/"/>">
								<fmt:message key="navigate.home"/>
							</a>
						</c:if>
					</li>
					<li>
						<c:if test="${navigationTab eq 'accounts'}">
							<strong>
								<a href="<c:url value="/accounts/search"/>">
									<fmt:message key="navigate.accounts"/>
								</a>
							</strong>
						</c:if>
						<c:if test="${navigationTab != 'accounts'}">
							<a href="<c:url value="/accounts/search"/>">
								<fmt:message key="navigate.accounts"/>
							</a>
						</c:if>
					</li>
					<li>
						<c:if test="${navigationTab eq 'newReward'}">
							<strong>
								<a href="<c:url value="/rewards/newReward"/>">
									<fmt:message key="navigate.rewards"/>
								</a>
							</strong>
						</c:if>
						<c:if test="${navigationTab != 'newReward'}">
							<a href="<c:url value="/rewards/newReward"/>">
								<fmt:message key="navigate.rewards"/>
							</a>
						</c:if>						
					</li>		
					<security:authorize ifNotGranted="ROLE_ANONYMOUS">
        	            <li>
	        	            <a href="<c:url value="/j_spring_security_logout"/>"><fmt:message key="navigate.logout"/></a>
    	                </li>
                    </security:authorize>
								
				</ul>
			</div>
		</div>
		<div id="footer" class="clearfix">
			<p><fmt:message key="footer.message"/></p>
		</div>
	</div>
</body>
</html>