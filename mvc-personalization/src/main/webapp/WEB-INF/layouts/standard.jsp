<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title><fmt:message><tiles:insertAttribute name="title"/></fmt:message></title>
	<!--  TODO-04: Use the theme to select the right stylesheet.  The themes are
	               defined by properties files in /WEB-INF/classes. Now you should
                   be able to change to a 'Blue' look.  The banner image is 
                   still green - see the next TO DO step below.-->
	<link type="text/css" rel="stylesheet" href="<c:url value="/styles/richweb-green.css" />" />
</head>
<body>
	<div id="page">
		<div id="header" class="clearfix">
			<div id="branding">
				<!-- 
				  // In previous labs, this logo was a background image defined
				  // by the stylesheet.  We have pulled it out here to let you
				  // practice using a theme.
				  -->
				  <!-- TODO-05: Fix the banner image to match the theme. -->
				<img src="<c:url value="/images/springsource_banner_green.png"/>" />
			</div>
			<div id="personalization">
				<c:choose>
					<c:when test="${requestContext.locale.language eq 'en'}">
						<c:url var="localeUrl" value="/">
							<c:param name="locale" value="fr"/>
						</c:url>
						<a href="${localeUrl}">Français</a>
					</c:when>
					<c:otherwise>
						<c:url var="localeUrl" value="/">
							<c:param name="locale" value="en"/>
						</c:url>
						<a href="${localeUrl}">English</a>
					</c:otherwise>
				</c:choose> |
				<!-- Initially no Themes are setup, so the default is 'theme'. 
                     Treat this a 'green' since that's the default look we've been using.  -->
				<c:choose>
					<c:when test="${requestContext.theme.name eq 'theme' or requestContext.theme.name eq 'green'}">
						<c:url var="themeUrl" value="/">
							<c:param name="theme" value="blue"/>
						</c:url>
						<a href="${themeUrl}"><fmt:message key="theme.Blue"/></a>
					</c:when>
					<c:otherwise>
						<c:url var="themeUrl" value="/">
							<c:param name="theme" value="green"/>
						</c:url>
						<a href="${themeUrl}"><fmt:message key="theme.Green"/></a>
					</c:otherwise>
				</c:choose>
			</div>
			<hr/>
		</div> <!-- end header -->
		<div id="content" class="clearfix">
			<div id="main">
				<tiles:insertAttribute name="main" />
				<hr/>
			</div> <!-- end main -->
			<div id="nav">
				<tiles:importAttribute name="navigationTab" />
				<div class="wrapper">
					<ul class="clearfix">
						<li><c:if test="${navigationTab eq 'home'}">
								<strong><a href="<c:url value="/"/>"><fmt:message key="navigate.home"/></a></strong>
							</c:if>
							<c:if test="${navigationTab != 'home'}">
								<a href="<c:url value="/"/>"><fmt:message key="navigate.home"/></a>
							</c:if>
						</li>
						<li><c:if test="${navigationTab eq 'accounts'}">
								<strong><a href="<c:url value="/accounts/search"/>"><fmt:message key="navigate.accounts"/></a></strong>
							</c:if>
							<c:if test="${navigationTab != 'accounts'}">
								<a href="<c:url value="/accounts/search"/>"><fmt:message key="navigate.accounts"/></a>
							</c:if>
						</li>
					</ul>
				</div>
			</div> <!-- end nav -->
		</div> <!-- end content -->
		<div id="footer" class="clearfix">
			<p><fmt:message key="footer.message"/></p>
		</div>
	</div>
</body>
</html>